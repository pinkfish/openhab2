package org.openhab.binding.elkm1.handler;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elkm1.ElkM1BindingConstants;
import org.openhab.binding.elkm1.internal.elk.ElkDefinition;
import org.openhab.binding.elkm1.internal.elk.ElkZoneConfig;
import org.openhab.binding.elkm1.internal.elk.ElkZoneStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElkM1ZoneHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(ElkM1ZoneHandler.class);

    public ElkM1ZoneHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    public void updateZoneConfig(ElkZoneConfig config, ElkZoneStatus status) {
        logger.error("Update zone config {} {}", config, status);
        Channel chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_ZONE_CONFIG);
        updateState(chan.getUID(), new StringType(config.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_ZONE_STATUS);
        updateState(chan.getUID(), new StringType(status.toString()));
        chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_ZONE_DEFINITION);
    }

    public void updateZoneDefinition(ElkDefinition definition) {
        logger.error("Update zone definition {}", definition);
        Channel chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_ZONE_DEFINITION);
        updateState(chan.getUID(), new StringType(definition.toString()));
    }

    public void updateZoneArea(int area) {
        logger.error("Update zone area {}", area);
        Channel chan = getThing().getChannel(ElkM1BindingConstants.CHANNEL_ZONE_AREA);
        updateState(chan.getUID(), new DecimalType(area));
    }
}
