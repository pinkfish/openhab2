package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

public class AlarmZoneReply extends ElkMessage {
    public AlarmZoneReply(String input) {
        super(ElkCommand.AlarmZoneRequestReply);

    }

    @Override
    protected String getData() {
        return null;
    }

}
