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
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkm1.internal.config.ElkAlarmConfig;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmConnection;
import org.openhab.binding.elkm1.internal.elk.ElkListener;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;
import org.openhab.binding.elkm1.internal.elk.ElkMessageFactory;
import org.openhab.binding.elkm1.internal.elk.message.VersionRequest;
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
        messageFactory = new ElkMessageFactory();
        ElkAlarmConfig config = getConfigAs(ElkAlarmConfig.class);
        connection = new ElkAlarmConnection(config, messageFactory);
        connection.addElkListener(this);
        if (connection.initialize()) {
            connection.sendCommand(new VersionRequest());
            updateStatus(ThingStatus.ONLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Requesting version from alarm");
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Unable to open socket to alarm");
        }
    }

    @Override
    public void handleElkMessage(ElkMessage message) {
        logger.info("Got elk message {}", message.toString());
    }
}
