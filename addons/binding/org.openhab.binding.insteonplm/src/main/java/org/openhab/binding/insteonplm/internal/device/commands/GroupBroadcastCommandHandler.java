package org.openhab.binding.insteonplm.internal.device.commands;

import java.io.IOException;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.CommandHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.InsteonFlags;
import org.openhab.binding.insteonplm.internal.message.Message;
import org.openhab.binding.insteonplm.internal.message.StandardInsteonMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends ALLLink broadcast commands to group
 *
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public class GroupBroadcastCommandHandler extends CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(GroupBroadcastCommandHandler.class);

    GroupBroadcastCommandHandler(DeviceFeature f) {
        super(f);
    }

    @Override
    public void handleCommand(InsteonThingHandler conf, ChannelUID channel, Command cmd) {
        try {
            if (cmd == OnOffType.ON || cmd == OnOffType.OFF) {
                StandardInsteonMessages cmd1 = ((cmd == OnOffType.ON) ? StandardInsteonMessages.LightOn
                        : StandardInsteonMessages.LightOff);
                byte value = (byte) ((cmd == OnOffType.ON) ? 0xFF : 0x00);
                int group = conf.getInsteonGroup();
                if (group == -1) {
                    logger.error("no group=xx specified in item {}", conf.getThing().getLabel());
                    return;
                }
                logger.info("{}: sending {} broadcast to group {}", nm(),
                        StandardInsteonMessages.LightOn == cmd1 ? "ON" : "OFF", conf.getInsteonGroup());
                Message m = conf.getMessageFactory().makeStandardMessage(new InsteonFlags(), cmd1, value, group,
                        conf.getAddress());
                conf.enqueueMessage(m);
            }
        } catch (IOException e) {
            logger.error("{}: command send i/o error: ", nm(), e);
        } catch (FieldException e) {
            logger.error("{}: command send message creation error ", nm(), e);
        }
    }
}