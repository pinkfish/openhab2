package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkDefinition;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Returns the definitions of all the zones from the elk.
 *
 * @author David Bennett - Initial COntribution
 */
public class ZoneDefitionReply extends ElkMessage {
    private ElkDefinition[] definition;

    public ZoneDefitionReply(String data) {
        super(ElkCommand.ZoneDefinitionReply);
        if (data.length() >= 208) {
            byte[] dataBytes = data.getBytes();
            definition = new ElkDefinition[208];
            for (int i = 0; i < 208; i++) {
                int val = dataBytes[i] - 0x30;
                if (val > ElkDefinition.values().length) {
                    definition[i] = ElkDefinition.Disabled;
                } else {
                    definition[i] = ElkDefinition.values()[val];
                }
            }
        }
    }

    public ElkDefinition[] getDefinition() {
        return definition;
    }

    @Override
    protected String getData() {
        // TODO Auto-generated method stub
        return null;
    }

}
