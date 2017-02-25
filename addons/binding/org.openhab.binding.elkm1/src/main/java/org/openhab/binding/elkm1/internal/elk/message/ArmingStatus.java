package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Request the current status of all the areas from the elk.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class ArmingStatus extends ElkMessage {
    public ArmingStatus() {
        super(ElkCommand.ArmingStatusRequest);
    }

    @Override
    protected String getData() {
        return "";
    }
}
