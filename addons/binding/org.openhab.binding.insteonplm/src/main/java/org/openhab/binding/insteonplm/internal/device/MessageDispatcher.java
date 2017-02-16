/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;

import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Message;
import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Does preprocessing of messages to decide which handler should be called.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public abstract class MessageDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    DeviceFeature m_feature = null;
    HashMap<String, String> m_parameters = new HashMap<String, String>();

    /**
     * Constructor
     *
     * @param f DeviceFeature to which this MessageDispatcher belongs
     */
    MessageDispatcher(DeviceFeature f) {
        m_feature = f;
    }

    public void setParameters(HashMap<String, String> hm) {
        m_parameters = hm;
    }

    /**
     * Generic handling of incoming ALL LINK messages
     *
     * @param msg the message received
     * @param port the port on which the message was received
     * @return true if the message was handled by this function
     */
    protected boolean handleAllLinkMessage(InsteonThingHandler handler, Message msg) {
        if (!msg.isAllLink()) {
            return false;
        }
        try {
            InsteonAddress a = msg.getAddress("toAddress");
            // ALL_LINK_BROADCAST and ALL_LINK_CLEANUP
            // have a valid Command1 field
            // but the CLEANUP_SUCCESS (of type ALL_LINK_BROADCAST!)
            // message has cmd1 = 0x06 and the cmd as the
            // high byte of the toAddress.
            byte cmd1 = msg.getByte("command1");
            if (!msg.isCleanup() && cmd1 == 0x06) {
                cmd1 = a.getHighByte();
            }
            // For ALL_LINK_BROADCAST messages, the group is
            // in the low byte of the toAddress. For direct
            // ALL_LINK_CLEANUP, it is in Command2

            int group = (msg.isCleanup() ? msg.getByte("command2") : a.getLowByte()) & 0xff;
            MessageHandler h = m_feature.getMsgHandlers().get(cmd1 & 0xFF);
            if (h == null) {
                h = m_feature.getDefaultMsgHandler();
            }
            logger.debug("all link message: {}", msg);
            if (!h.isDuplicate(msg)) {
                logger.debug("all link message is no duplicate: {}/{}", h.matchesGroup(group), h.matches(msg));
                if (h.matchesGroup(group) && h.matches(msg)) {
                    logger.debug("{}:{}->{} cmd1:{} group {}/{}:{}", handler.getAddress(), m_feature.getName(),
                            h.getClass().getSimpleName(), Utils.getHexByte(cmd1), group, h.getGroup(), msg);
                    h.handleMessage(handler, group, cmd1, msg, m_feature);
                }
            }
        } catch (FieldException e) {
            logger.error("couldn't parse ALL_LINK message: {}", msg, e);
        }
        return true;
    }

    /**
     * Checks if this message is in response to previous query by this feature
     *
     * @param msg
     * @return true;
     */
    boolean isMyDirectAck(InsteonThingHandler handler, Message msg) {
        return msg.isAckOfDirect() && (m_feature.getQueryStatus() == DeviceFeature.QueryStatus.QUERY_PENDING)
                && handler.getFeatureQueried() == m_feature;
    }

    /**
     * Dispatches message
     *
     * @param handler The handler the message came from
     * @param msg Message to dispatch
     * @return true if this message was found to be a reply to a direct message,
     *         and was claimed by one of the handlers
     */
    public abstract boolean dispatch(InsteonThingHandler handler, Message msg);

    //
    //
    // ------------ implementations of MessageDispatchers start here ------------------
    //
    //

    public static class DefaultDispatcher extends MessageDispatcher {
        DefaultDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            byte cmd = 0x00;
            byte cmd1 = 0x00;
            boolean isConsumed = false;
            int key = -1;
            try {
                cmd = msg.getByte("Cmd");
                cmd1 = msg.getByte("command1");
            } catch (FieldException e) {
                logger.debug("no command found, dropping msg {}", msg);
                return false;
            }
            if (msg.isAllLinkCleanupAckOrNack()) {
                // Had cases when a KeypadLinc would send an ALL_LINK_CLEANUP_ACK
                // in response to a direct status query message
                return false;
            }
            if (handleAllLinkMessage(handler, msg)) {
                return false;
            }
            if (msg.isAckOfDirect()) {
                // in the case of direct ack, the cmd1 code is useless.
                // you have to know what message was sent before to
                // interpret the reply message
                if (isMyDirectAck(handler, msg)) {
                    logger.debug("{}:{} DIRECT_ACK: q:{} cmd: {}", handler.getAddress(), m_feature.getName(),
                            m_feature.getQueryStatus(), cmd);
                    isConsumed = true;
                    if (cmd == 0x50) {
                        // must be a reply to our message, tweak the cmd1 code!
                        logger.debug("changing key to 0x19 for msg {}", msg);
                        key = 0x19; // we have installed a handler under that command number
                    }
                }
            } else {
                key = (cmd1 & 0xFF);
            }
            if (key != -1 || m_feature.isStatusFeature()) {
                MessageHandler h = m_feature.getMsgHandlers().get(key);
                if (h == null) {
                    h = m_feature.getDefaultMsgHandler();
                }
                if (h.matches(msg)) {
                    if (!isConsumed) {
                        logger.debug("{}:{}->{} DIRECT", handler.getAddress(), m_feature.getName(),
                                h.getClass().getSimpleName());
                    }
                    h.handleMessage(handler, -1, cmd1, msg, m_feature);
                }
            }
            if (isConsumed) {
                m_feature.setQueryStatus(DeviceFeature.QueryStatus.QUERY_ANSWERED);
                logger.debug("defdisp: {}:{} set status to: {}", handler.getAddress(), m_feature.getName(),
                        m_feature.getQueryStatus());
            }
            return isConsumed;
        }
    }

    public static class DefaultGroupDispatcher extends MessageDispatcher {
        DefaultGroupDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            byte cmd = 0x00;
            byte cmd1 = 0x00;
            boolean isConsumed = false;
            int key = -1;
            try {
                cmd = msg.getByte("Cmd");
                cmd1 = msg.getByte("command1");
            } catch (FieldException e) {
                logger.debug("no command found, dropping msg {}", msg);
                return false;
            }
            if (msg.isAllLinkCleanupAckOrNack()) {
                // Had cases when a KeypadLinc would send an ALL_LINK_CLEANUP_ACK
                // in response to a direct status query message
                return false;
            }
            if (handleAllLinkMessage(handler, msg)) {
                return false;
            }
            if (msg.isAckOfDirect()) {
                // in the case of direct ack, the cmd1 code is useless.
                // you have to know what message was sent before to
                // interpret the reply message
                if (isMyDirectAck(handler, msg)) {
                    logger.debug("{}:{} qs:{} cmd: {}", handler.getAddress(), m_feature.getName(),
                            m_feature.getQueryStatus(), cmd);
                    isConsumed = true;
                    if (cmd == 0x50) {
                        // must be a reply to our message, tweak the cmd1 code!
                        logger.debug("changing key to 0x19 for msg {}", msg);
                        key = 0x19; // we have installed a handler under that command number
                    }
                }
            } else {
                key = (cmd1 & 0xFF);
            }
            /*
             * if (key != -1) {
             * for (DeviceFeature f : m_feature.getConnectedFeatures()) {
             * MessageHandler h = f.getMsgHandlers().get(key);
             * if (h == null) {
             * h = f.getDefaultMsgHandler();
             * }
             * if (h.matches(msg)) {
             * if (!isConsumed) {
             * logger.debug("{}:{}->{} DIRECT", f.getDevice().getAddress(), f.getName(),
             * h.getClass().getSimpleName());
             * }
             * h.handleMessage(-1, cmd1, msg, f, port);
             * }
             *
             * }
             * }
             */
            if (isConsumed) {
                m_feature.setQueryStatus(DeviceFeature.QueryStatus.QUERY_ANSWERED);
                logger.debug("{}:{} set status to: {}", handler.getAddress(), m_feature.getName(),
                        m_feature.getQueryStatus());
            }
            return isConsumed;
        }
    }

    public static class PollGroupDispatcher extends MessageDispatcher {
        PollGroupDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            if (msg.isAllLinkCleanupAckOrNack()) {
                // Had cases when a KeypadLinc would send an ALL_LINK_CLEANUP_ACK
                // in response to a direct status query message
                return false;
            }
            if (handleAllLinkMessage(handler, msg)) {
                return false;
            }
            if (msg.isAckOfDirect()) {
                boolean isMyAck = isMyDirectAck(handler, msg);
                if (isMyAck) {
                    logger.debug("{}:{} got poll ACK", handler.getAddress(), m_feature.getName());
                }
                return (isMyAck);
            }
            return (false); // not a direct ack, so we didn't consume it either
        }
    }

    public static class SimpleDispatcher extends MessageDispatcher {
        SimpleDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            byte cmd1 = 0x00;
            try {
                if (handleAllLinkMessage(handler, msg)) {
                    return false;
                }
                if (msg.isAllLinkCleanupAckOrNack()) {
                    // Had cases when a KeypadLinc would send an ALL_LINK_CLEANUP_ACK
                    // in response to a direct status query message
                    return false;
                }
                cmd1 = msg.getByte("command1");
            } catch (FieldException e) {
                logger.debug("no cmd1 found, dropping msg {}", msg);
                return false;
            }
            boolean isConsumed = isMyDirectAck(handler, msg);
            int key = (cmd1 & 0xFF);
            MessageHandler h = m_feature.getMsgHandlers().get(key);
            if (h == null) {
                h = m_feature.getDefaultMsgHandler();
            }
            if (h.matches(msg)) {
                logger.trace("{}:{}->{} {}", handler.getAddress(), m_feature.getName(), h.getClass().getSimpleName(),
                        msg);
                h.handleMessage(handler, -1, cmd1, msg, m_feature);
            }
            return isConsumed;
        }
    }

    public static class X10Dispatcher extends MessageDispatcher {
        X10Dispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            try {
                byte rawX10 = msg.getByte("rawX10");
                int cmd = (rawX10 & 0x0f);
                MessageHandler h = m_feature.getMsgHandlers().get(cmd);
                if (h == null) {
                    h = m_feature.getDefaultMsgHandler();
                }
                logger.debug("{}:{}->{} {}", handler.getAddress(), m_feature.getName(), h.getClass().getSimpleName(),
                        msg);
                if (h.matches(msg)) {
                    h.handleMessage(handler, -1, (byte) cmd, msg, m_feature);
                }
            } catch (FieldException e) {
                logger.error("error parsing {}: ", msg, e);
            }
            return false;
        }
    }

    public static class PassThroughDispatcher extends MessageDispatcher {
        PassThroughDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            MessageHandler h = m_feature.getDefaultMsgHandler();
            if (h.matches(msg)) {
                logger.trace("{}:{}->{} {}", handler.getAddress(), m_feature.getName(), h.getClass().getSimpleName(),
                        msg);
                h.handleMessage(handler, -1, (byte) 0x01, msg, m_feature);
            }
            return false;
        }
    }

    /**
     * Drop all incoming messages silently
     */
    public static class NoOpDispatcher extends MessageDispatcher {
        NoOpDispatcher(DeviceFeature f) {
            super(f);
        }

        @Override
        public boolean dispatch(InsteonThingHandler handler, Message msg) {
            return false;
        }
    }
}
