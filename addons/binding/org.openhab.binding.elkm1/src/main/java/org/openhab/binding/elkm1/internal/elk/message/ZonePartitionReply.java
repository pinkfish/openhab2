package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Asks the elk for the zone partitions for all the zones. The partition
 * is which area it is associated with.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class ZonePartitionReply extends ElkMessage {
    int[] areas;

    public ZonePartitionReply(String data) {
        super(ElkCommand.ZonePartitionReply);
        areas = new int[208];
        byte[] dataBytes = data.getBytes();
        for (int i = 0; i < 208; i++) {
            areas[i] = dataBytes[i] - 0x30;
        }
    }

    public int[] getAreas() {
        return areas;
    }

    @Override
    protected String getData() {
        return null;
    }
}
