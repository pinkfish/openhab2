package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Requests the status of the zones from the elk system
 *
 * @author David Bennett - Initial contribution
 */
public class ZoneStatus extends ElkMessage {
    public ZoneStatus() {
        super(ElkCommand.ZoneStatusRequest);
    }

    @Override
    protected String getData() {
        return "";
    }
}
