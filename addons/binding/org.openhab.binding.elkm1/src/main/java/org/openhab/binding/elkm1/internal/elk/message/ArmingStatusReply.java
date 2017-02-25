package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkAlarmAreaState;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmArmUpState;
import org.openhab.binding.elkm1.internal.elk.ElkAlarmArmedState;
import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;

/**
 * Although this is technically a status reply, it can come at any time when the status changes.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class ArmingStatusReply extends ElkMessage {
    private ElkAlarmArmUpState[] armedUp;
    private ElkAlarmAreaState[] state;
    private ElkAlarmArmedState[] armed;

    public ArmingStatusReply(String data) {
        super(ElkCommand.ArmingStatusRequestReply);
        byte[] dataBytes = data.getBytes();
        for (int i = 0; i < 8; i++) {
            armed[i] = ElkAlarmArmedState.values()[dataBytes[i] - 0x30];
            armedUp[i] = ElkAlarmArmUpState.values()[dataBytes[i + 8] - 0x30];
            state[i] = ElkAlarmAreaState.values()[dataBytes[i + 16] - 0x30];
        }
    }

    public ElkAlarmArmUpState[] getArmedUp() {
        return armedUp;
    }

    public ElkAlarmAreaState[] getState() {
        return state;
    }

    public ElkAlarmArmedState[] getArmed() {
        return armed;
    }

    @Override
    protected String getData() {
        // TODO Auto-generated method stub
        return null;
    }

}
