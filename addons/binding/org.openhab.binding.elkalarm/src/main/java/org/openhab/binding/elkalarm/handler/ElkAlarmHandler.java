/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkalarm.handler;

import static org.openhab.binding.elkalarm.ElkAlarmBindingConstants.CHANNEL_1;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkalarm.internal.config.ElkAlarmConfig;
import org.openhab.binding.elkalarm.internal.elk.ElkAlarmConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ElkAlarmHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author David Bennett - Initial contribution
 */
public class ElkAlarmHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(ElkAlarmHandler.class);

    private ElkAlarmConnection connection;

    public ElkAlarmHandler(Bridge thing) {
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
        connection = new ElkAlarmConnection(config);
    }
}
