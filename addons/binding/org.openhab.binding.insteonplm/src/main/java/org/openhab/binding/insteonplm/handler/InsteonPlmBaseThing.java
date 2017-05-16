package org.openhab.binding.insteonplm.handler;

import java.io.IOException;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.openhab.binding.insteonplm.internal.driver.Port;

public abstract class InsteonPlmBaseThing extends BaseThingHandler {

    public InsteonPlmBaseThing(Thing thing) {
        super(thing);
    }

    public abstract long processRequestQueue(Port port, long now) throws IOException;
}
