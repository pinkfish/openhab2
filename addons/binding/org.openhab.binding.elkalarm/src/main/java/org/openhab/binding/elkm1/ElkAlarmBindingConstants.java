/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkm1;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link ElkAlarmBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author David Bennett - Initial contribution
 */
public class ElkAlarmBindingConstants {

    public static final String BINDING_ID = "elkalarm";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_BRIDGE = new ThingTypeUID(BINDING_ID, "bridge");
    public final static ThingTypeUID THING_TYPE_ZONE = new ThingTypeUID(BINDING_ID, "zone");

    // List of all the properties on the things.
    public static final String PROPERTY_VERSION_NUMBER = "version";

    // List of all Channel ids
    public static final String CHANNEL_ZONE_STATUS = "status";
    public static final String CHANNEL_ZONE_ALARMED = "alarmed";
    public static final String CHANNEL_ZONE_CONFIG = "config";

}
