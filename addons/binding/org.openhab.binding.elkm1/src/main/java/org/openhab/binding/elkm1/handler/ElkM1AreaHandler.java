/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkm1.handler;

import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkm1.ElkM1BindingConstants;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmAreaState;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmArmUpState;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmArmedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The elk area handler to handle the area updates.
 *
 * @author David Bennett - Initial Contribution
 */
public class ElkM1AreaHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(ElkM1AreaHandler.class);

    public ElkM1AreaHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(ElkM1BindingConstants.CHANNEL_AREA_ARMED)) {
            StringType str = (StringType) command;
            // Changing the armed state.
            ElkAlarmArmedState armed = ElkAlarmArmedState.valueOf(str.toString());
            ElkM1BridgeHandler bridgeHandler = (ElkM1BridgeHandler) getBridge().getHandler();
            int zone = Integer.valueOf(getThing().getProperties().get(ElkM1BindingConstants.PROPERTY_ZONE_NUM));
            bridgeHandler.updateArmedState(zone, armed);
        }
    }

    public void updateArea(ElkAlarmAreaState state, ElkAlarmArmedState armed, ElkAlarmArmUpState armUp) {
        logger.debug("Update area config {} {} {}", state, armed, armUp);
        Channel chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_STATE);
        updateState(chan.getUID(), new StringType(state.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_ARMED);
        updateState(chan.getUID(), new StringType(armed.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_ARMUP);
        updateState(chan.getUID(), new StringType(armUp.toString()));
    }
}
