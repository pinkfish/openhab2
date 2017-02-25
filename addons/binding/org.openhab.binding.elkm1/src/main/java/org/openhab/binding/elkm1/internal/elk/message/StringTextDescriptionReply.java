package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;
import org.openhab.binding.elkm1.internal.elk.ElkTypeToRequest;

/**
 * The response to a request to get the elk message data.
 *
 * @author David Bennett - Initial Contribution
 */
public class StringTextDescriptionReply extends ElkMessage {
    private ElkTypeToRequest typeResponse;
    private int thingNum;
    private String text;

    public StringTextDescriptionReply(String input) {
        super(ElkCommand.StringTextDescriptionReply);
        typeResponse = ElkTypeToRequest.values()[Integer.valueOf(input.substring(0, 2), 16)];
        thingNum = Integer.valueOf(input.substring(2, 5));
        text = input.substring(5).trim();
    }

    public ElkTypeToRequest getTypeResponse() {
        return typeResponse;
    }

    public int getThingNum() {
        return thingNum;
    }

    public String getText() {
        return text;
    }

    @Override
    protected String getData() {
        return null;
    }

}
