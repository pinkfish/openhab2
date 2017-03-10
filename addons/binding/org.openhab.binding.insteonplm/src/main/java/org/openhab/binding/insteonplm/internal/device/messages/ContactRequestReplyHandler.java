package org.openhab.binding.insteonplm.internal.device.messages;

import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.Channel;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.MessageHandler;
import org.openhab.binding.insteonplm.internal.message.modem.StandardMessageReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public class ContactRequestReplyHandler extends MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(ContactRequestReplyHandler.class);

    ContactRequestReplyHandler(DeviceFeature p) {
        super(p);
    }

    @Override
    public void handleMessage(InsteonThingHandler handler, int group, StandardMessageReceived msg, Channel f) {
        if (msg.getFlags().isAckOfDirect()) {
            OpenClosedType oc = (msg.getCmd2() == 0) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
            logger.info("{}: set contact {} to: {}", nm(), handler.getAddress(), oc);
            handler.updateFeatureState(f, oc);
        }
    }
}
