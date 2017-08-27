package org.openhab.binding.insteonplm.internal.device;

import org.eclipse.smarthome.core.events.AbstractEvent;

/**
 * The event to send when the plm does something nifty.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class InsteonPLMEvent extends AbstractEvent {
    public InsteonPLMEvent(String topic, String payload, String source) {
        super(topic, payload, source);
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
