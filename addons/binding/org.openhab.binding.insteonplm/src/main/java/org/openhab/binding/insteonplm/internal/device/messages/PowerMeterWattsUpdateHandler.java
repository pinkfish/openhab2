package org.openhab.binding.insteonplm.internal.device.messages;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Channel;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.MessageHandler;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public class PowerMeterWattsUpdateHandler extends MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(PowerMeterWattsUpdateHandler.class);

    PowerMeterWattsUpdateHandler(DeviceFeature p) {
        super(p);
    }

    @Override
    public void handleMessage(InsteonThingHandler handler, int group, byte cmd1, Message msg, Channel f) {
        if (msg.isExtended()) {
            try {
                // see iMeter developer notes 2423A1dev-072013-en.pdf
                int b7 = msg.getByte("userData7") & 0xff;
                int b8 = msg.getByte("userData8") & 0xff;
                int watts = (b7 << 8) | b8;
                if (watts > 32767) {
                    watts -= 65535;
                }

                int b9 = msg.getByte("userData9") & 0xff;
                int b10 = msg.getByte("userData10") & 0xff;
                int b11 = msg.getByte("userData11") & 0xff;
                int b12 = msg.getByte("userData12") & 0xff;
                BigDecimal kwh = BigDecimal.ZERO;
                if (b9 < 254) {
                    int e = (b9 << 24) | (b10 << 16) | (b11 << 8) | b12;
                    kwh = new BigDecimal(e * 65535.0 / (1000 * 60 * 60 * 60)).setScale(4, RoundingMode.HALF_UP);
                }

                logger.debug("{}:{} watts: {} kwh: {} ", nm(), handler.getAddress(), watts, kwh);
                handler.updateFeatureState(f, new DecimalType(watts));
            } catch (FieldException e) {
                logger.error("error parsing {}: ", msg, e);
            }
        }
    }
}
