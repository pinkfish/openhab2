/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.handler;

import static org.openhab.binding.nest.NestBindingConstants.*;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link NestThermostatHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 * 
 * @author David Bennett - Initial contribution
 */
public class NestThermostatHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(NestThermostatHandler.class);

	public NestThermostatHandler(Thing thing) {
		super(thing);
	}

	@Override
	public void handleCommand(ChannelUID channelUID, Command command) {
        if(channelUID.getId().equals(CHANNEL_MODE)) {
        }
        if(channelUID.getId().equals(CHANNEL_MAX_SET_POINT)) {
        }
        if(channelUID.getId().equals(CHANNEL_MIN_SET_POINT)) {
        }
	}

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.
        updateStatus(ThingStatus.ONLINE);
    }
}
