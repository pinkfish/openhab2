package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Get the version number from the elk.
 *
 * @author David Bennett - Initial Contribution
 */
public class Version extends ElkMessage {
    public Version() {
        super(ElkCommand.RequestVersionNumber);
    }

    @Override
    protected String getData() {
        return "";
    }
}
