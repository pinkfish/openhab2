package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Requests the alarm zone info from the elk m1 gold.
 *
 * @author David Bennett - Initial Contribution
 */
public class AlarmZone extends ElkMessage {
    public AlarmZone() {
        super(ElkCommand.AlarmZoneRequest);
    }

    @Override
    protected String getData() {
        return "";
    }
}
