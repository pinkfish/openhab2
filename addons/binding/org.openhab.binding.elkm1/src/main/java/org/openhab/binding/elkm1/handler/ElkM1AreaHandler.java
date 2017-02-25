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

public class ElkM1AreaHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(ElkM1AreaHandler.class);

    public ElkM1AreaHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    public void updateArea(ElkAlarmAreaState state, ElkAlarmArmedState armed, ElkAlarmArmUpState armUp) {
        logger.error("Update area config {} {} {}", state, armed, armUp);
        Channel chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_STATE);
        updateState(chan.getUID(), new StringType(state.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_ARMED);
        updateState(chan.getUID(), new StringType(armed.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_AREA_ARMUP);
        updateState(chan.getUID(), new StringType(armUp.toString()));
    }
}
