package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * The zone definition request to get the details about the zone.
 *
 * @author David Bennett - Initial Contribution
 */
public class ZoneDefinition extends ElkMessage {
    public ZoneDefinition() {
        super(ElkCommand.ZoneDefintionRequest);
    }

    @Override
    protected String getData() {
        return "";
    }
}
