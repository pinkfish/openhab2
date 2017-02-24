package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * The result from elk with the version details in it.
 *
 * @author David Bennett - Initial Contribution
 */
public class VersionReply extends ElkMessage {
    private String versionMax;
    private String versionMiddle;
    private String versionLow;

    public VersionReply(String incomingData) {
        super(ElkCommand.RequestVersionNumberReply);
        versionMax = incomingData.substring(0, 2);
        versionMiddle = incomingData.substring(2, 4);
        versionLow = incomingData.substring(4, 6);
    }

    /**
     * The version number of this elk.
     */
    String getElkVersion() {
        return versionMax + "." + versionMiddle + "." + versionLow;
    }

    @Override
    protected String getData() {
        // TODO Auto-generated method stub
        return null;
    }

}
