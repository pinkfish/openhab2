/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkm1.handler;

import static org.openhab.binding.elkm1.ElkAlarmBindingConstants.CHANNEL_1;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkm1.ElkAlarmBindingConstants;
import org.openhab.binding.elkm1.internal.config.ElkAlarmConfig;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmConnection;
import org.openhab.binding.elkm1.internal.elk.ElkListener;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;
import org.openhab.binding.elkm1.internal.elk.ElkMessageFactory;
import org.openhab.binding.elkm1.internal.elk.message.VersionReply;
import org.openhab.binding.elkm1.internal.elk.message.ZoneChangeUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ElkM1Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author David Bennett - Initial contribution
 */
public class ElkM1Handler extends BaseBridgeHandler implements ElkListener {

    private Logger logger = LoggerFactory.getLogger(ElkM1Handler.class);

    private ElkAlarmConnection connection;
    private ElkMessageFactory messageFactory;

    public ElkM1Handler(Bridge thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(CHANNEL_1)) {
        }
    }

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.
        updateStatus(ThingStatus.ONLINE);

        // Load up the config and then get the connection to the elk setup.
        ElkAlarmConfig config = getConfigAs(ElkAlarmConfig.class);
        this.connection = new ElkAlarmConnection(config, messageFactory);
        this.connection.addElkListener(this);
    }

    @Override
    public void handleElkMessage(ElkMessage message) {
        switch (message.getElkCommand()) {
            case RequestVersionNumberReply:
                VersionReply reply = (VersionReply) message;
                // Yay, have a version number.
                getThing().setProperty(ElkAlarmBindingConstants.PROPERTY_VERSION_NUMBER, reply.getElkVersion());
                break;
            case ZoneChangeUpdateReport:
                ZoneChangeUpdate update = (ZoneChangeUpdate) message;
                // Find the thing associated with the zone.
                Thing zone = getThingForZone(update.getZoneNumber());
                ElkM1ZoneHandler zoneHandler = zone.getHandler();

                break;
        }

    }

    private Thing getThingForZone(int zoneNumber) {
        // TODO Auto-generated method stub
        return null;
    }
}
