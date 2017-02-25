package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Acknowledge the ethernet response to us.
 *
 * @author David Bennett - Initial Contribution
 */
public class EthernetModuleTestReply extends ElkMessage {
    public EthernetModuleTestReply() {
        super(ElkCommand.EthernetModuleTestAcknowledge);
    }

    @Override
    protected String getData() {
        return "";
    }
}
