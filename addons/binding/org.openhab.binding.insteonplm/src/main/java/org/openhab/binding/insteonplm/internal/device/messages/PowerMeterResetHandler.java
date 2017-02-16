package org.openhab.binding.insteonplm.internal.device.messages;

import org.eclipse.smarthome.core.thing.Channel;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.MessageHandler;
import org.openhab.binding.insteonplm.internal.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public class PowerMeterResetHandler extends MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(PowerMeterResetHandler.class);

    PowerMeterResetHandler(DeviceFeature p) {
        super(p);
    }

    @Override
    public void handleMessage(InsteonThingHandler handler, int group, byte cmd1, Message msg, Channel f) {
        logger.info("{}: power meter {} was reset", nm(), handler.getAddress());

        // poll device to get updated kilowatt hours and watts
        handler.pollFeature(getFeature(), true);
    }
}
