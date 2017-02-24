package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Get the version number from the elk.
 *
 * @author David Bennett - Initial Contribution
 */
public class VersionRequest extends ElkMessage {
    public VersionRequest() {
        super(ElkCommand.RequestVersionNumber);
    }

    @Override
    protected String getData() {
        return "";
    }
}
