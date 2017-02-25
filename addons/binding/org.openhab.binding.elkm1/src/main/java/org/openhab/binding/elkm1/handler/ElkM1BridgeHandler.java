/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkm1.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkm1.ElkM1BindingConstants;
import org.openhab.binding.elkm1.internal.ElkM1HandlerListener;
import org.openhab.binding.elkm1.internal.config.ElkAlarmConfig;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmConnection;
import org.openhab.binding.elkm1.internal.elk.ElkDefinition;
import org.openhab.binding.elkm1.internal.elk.ElkListener;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;
import org.openhab.binding.elkm1.internal.elk.ElkMessageFactory;
import org.openhab.binding.elkm1.internal.elk.ElkTypeToRequest;
import org.openhab.binding.elkm1.internal.elk.ElkZoneConfig;
import org.openhab.binding.elkm1.internal.elk.ElkZoneStatus;
import org.openhab.binding.elkm1.internal.elk.message.ArmingStatus;
import org.openhab.binding.elkm1.internal.elk.message.ArmingStatusReply;
import org.openhab.binding.elkm1.internal.elk.message.EthernetModuleTest;
import org.openhab.binding.elkm1.internal.elk.message.EthernetModuleTestReply;
import org.openhab.binding.elkm1.internal.elk.message.StringTextDescription;
import org.openhab.binding.elkm1.internal.elk.message.StringTextDescriptionReply;
import org.openhab.binding.elkm1.internal.elk.message.Version;
import org.openhab.binding.elkm1.internal.elk.message.VersionReply;
import org.openhab.binding.elkm1.internal.elk.message.ZoneChangeUpdate;
import org.openhab.binding.elkm1.internal.elk.message.ZoneDefinition;
import org.openhab.binding.elkm1.internal.elk.message.ZoneDefitionReply;
import org.openhab.binding.elkm1.internal.elk.message.ZonePartition;
import org.openhab.binding.elkm1.internal.elk.message.ZonePartitionReply;
import org.openhab.binding.elkm1.internal.elk.message.ZoneStatus;
import org.openhab.binding.elkm1.internal.elk.message.ZoneStatusReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * The {@link ElkM1BridgeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author David Bennett - Initial contribution
 */
public class ElkM1BridgeHandler extends BaseBridgeHandler implements ElkListener {
    private Logger logger = LoggerFactory.getLogger(ElkM1BridgeHandler.class);

    private ElkAlarmConnection connection;
    private ElkMessageFactory messageFactory;
    private ZoneDetails[] zoneDetails = new ZoneDetails[208];
    private String[] areas = new String[8];
    private List<ElkM1HandlerListener> listeners = Lists.newArrayList();

    public ElkM1BridgeHandler(Bridge thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void initialize() {
        // Long running initialization should be done asynchronously in background.
        updateStatus(ThingStatus.ONLINE);

        // Add in something to all the zone details bits.
        for (int i = 0; i < 208; i++) {
            zoneDetails[i] = new ZoneDetails();
        }

        // Load up the config and then get the connection to the elk setup.
        messageFactory = new ElkMessageFactory();
        ElkAlarmConfig config = getConfigAs(ElkAlarmConfig.class);
        connection = new ElkAlarmConnection(config, messageFactory);
        connection.addElkListener(this);
        if (connection.initialize()) {
            connection.sendCommand(new Version());
            connection.sendCommand(new ZoneDefinition());
            connection.sendCommand(new ZonePartition());
            connection.sendCommand(new ZoneStatus());
            connection.sendCommand(new ArmingStatus());
            updateStatus(ThingStatus.ONLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Requesting version from alarm");
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Unable to open socket to alarm");
        }
    }

    @Override
    public void dispose() {
        connection.shutdown();
        zoneDetails = null;
        super.dispose();
    }

    @Override
    public void handleElkMessage(ElkMessage message) {
        logger.info("Got elk message {}", message.toString());
        if (message instanceof VersionReply) {
            VersionReply reply = (VersionReply) message;
            // Set the property.
            getThing().setProperty(ElkM1BindingConstants.PROPERTY_VERSION, reply.getElkVersion());
        }
        if (message instanceof ZoneStatusReply) {
            ZoneStatusReply reply = (ZoneStatusReply) message;
            for (int i = 0; i < 208; i++) {
                zoneDetails[i].config = reply.getConfig()[i];
                zoneDetails[i].status = reply.getStatus()[i];
                Thing thing = getThingForType(ElkTypeToRequest.Zone, i + 1);
                if (thing != null) {
                    ElkM1ZoneHandler handler = (ElkM1ZoneHandler) thing.getHandler();
                    handler.updateZoneConfig(zoneDetails[i].config, zoneDetails[i].status);
                }
            }
        }
        if (message instanceof ZonePartitionReply) {
            ZonePartitionReply reply = (ZonePartitionReply) message;
            for (int i = 0; i < 208; i++) {
                zoneDetails[i].area = reply.getAreas()[i];
                Thing thing = getThingForType(ElkTypeToRequest.Area, reply.getAreas()[i]);
                if (thing == null) {
                    // Request the area.
                    connection.sendCommand(new StringTextDescription(ElkTypeToRequest.Area, reply.getAreas()[i]));
                    logger.error("Requesting area {}", reply.getAreas()[i]);
                }
                thing = getThingForType(ElkTypeToRequest.Zone, i + 1);
                if (thing != null) {
                    ElkM1ZoneHandler handler = (ElkM1ZoneHandler) thing.getHandler();
                    handler.updateZoneArea(zoneDetails[i].area);
                }
            }
        }
        if (message instanceof ZoneDefitionReply) {
            ZoneDefitionReply reply = (ZoneDefitionReply) message;
            for (int i = 0; i < 208; i++) {
                zoneDetails[i].defintion = reply.getDefinition()[i];
                if (zoneDetails[i].defintion != ElkDefinition.Disabled) {
                    connection.sendCommand(new StringTextDescription(ElkTypeToRequest.Zone, i + 1));
                    logger.debug("Requesting {}", i);
                }
                Thing thing = getThingForType(ElkTypeToRequest.Zone, i + 1);
                if (thing != null) {
                    ElkM1ZoneHandler handler = (ElkM1ZoneHandler) thing.getHandler();
                    handler.updateZoneDefinition(zoneDetails[i].defintion);
                }
            }
        }
        if (message instanceof ZoneChangeUpdate) {
            ZoneChangeUpdate reply = (ZoneChangeUpdate) message;
            ZoneDetails details = zoneDetails[reply.getZoneNumber() - 1];
            details.status = reply.getStatus();
            details.config = reply.getConfig();
            Thing thing = getThingForType(ElkTypeToRequest.Zone, reply.getZoneNumber());
            if (thing != null) {
                ElkM1ZoneHandler handler = (ElkM1ZoneHandler) thing.getHandler();
                handler.updateZoneConfig(details.config, details.status);
            }
        }
        if (message instanceof EthernetModuleTest) {
            connection.sendCommand(new EthernetModuleTestReply());
        }
        if (message instanceof ArmingStatusReply) {
            ArmingStatusReply reply = (ArmingStatusReply) message;
            // Do stuff.
            for (int i = 0; i < 8; i++) {
                Thing thing = getThingForType(ElkTypeToRequest.Zone, i + 1);
                if (thing != null) {
                    ElkM1AreaHandler handler = (ElkM1AreaHandler) thing.getHandler();
                    handler.updateArea(reply.getState()[i], reply.getArmed()[i], reply.getArmedUp()[i]);
                }
            }
        }
        if (message instanceof StringTextDescriptionReply) {
            StringTextDescriptionReply reply = (StringTextDescriptionReply) message;
            switch (reply.getTypeResponse()) {
                case Zone:
                    ZoneDetails details = zoneDetails[reply.getThingNum() - 1];
                    details.label = reply.getText();
                    // Once we have a description, see if this zone exists.
                    Thing thing = getThingForType(ElkTypeToRequest.Zone, reply.getThingNum());
                    if (thing == null) {
                        for (ElkM1HandlerListener listener : this.listeners) {
                            listener.onZoneDiscovered(reply.getThingNum(), details.label);
                        }
                    }
                    break;
                case Area:
                    thing = getThingForType(ElkTypeToRequest.Area, reply.getThingNum());
                    if (thing == null) {
                        for (ElkM1HandlerListener listener : this.listeners) {
                            listener.onAreaDiscovered(reply.getThingNum(), reply.getText());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void addListener(ElkM1HandlerListener listener) {
        synchronized (listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(ElkM1HandlerListener listener) {
        synchronized (listeners) {
            this.listeners.remove(listener);
        }
    }

    Thing getThingForType(ElkTypeToRequest type, int num) {
        for (Thing thing : getThing().getThings()) {
            Map<String, String> properties = thing.getProperties();
            if (properties.get(ElkM1BindingConstants.PROPERTY_TYPE_ID).equals(type.toString())) {
                if (properties.get(ElkM1BindingConstants.PROPERTY_ZONE_NUM).equals(Integer.toString(num))) {
                    return thing;
                }
            }
        }
        return null;
    }

    class ZoneDetails {
        ElkZoneConfig config;
        ElkZoneStatus status;
        ElkDefinition defintion;
        String label;
        int area;
    }

    public void startScan() {
        connection.sendCommand(new ZoneStatus());
    }
}
