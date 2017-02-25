package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Asks the elk for the zone partititions for all the zones. The partition
 * is which area it is associated with.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class ZonePartition extends ElkMessage {
    public ZonePartition() {
        super(ElkCommand.ZonePartition);
    }

    @Override
    protected String getData() {
        return "";
    }

}
