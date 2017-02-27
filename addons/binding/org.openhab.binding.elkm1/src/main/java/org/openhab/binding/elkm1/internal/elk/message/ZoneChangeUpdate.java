/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elkm1.internal.elk.message;

import org.openhab.binding.elkm1.internal.elk.ElkCommand;
import org.openhab.binding.elkm1.internal.elk.ElkMessage;
import org.openhab.binding.elkm1.internal.elk.ElkZoneConfig;
import org.openhab.binding.elkm1.internal.elk.ElkZoneStatus;

/**
 * Called by the elk when the zone changes.
 *
 * @author David Bennett - Initial Contribution
 *
 */
public class ZoneChangeUpdate extends ElkMessage {
    ElkZoneConfig config;
    ElkZoneStatus status;
    int zoneNumber;

    public ZoneChangeUpdate(String incomingData) {
        super(ElkCommand.ZoneChangeUpdateReport);

        String zoneNumberStr = incomingData.substring(0, 3);
        zoneNumber = Integer.valueOf(zoneNumberStr, 16);
        String statusStr = incomingData.substring(3, 4);
        int value = Byte.valueOf(statusStr, 16);
        switch (value & 0x03) {
            case 0:
                config = ElkZoneConfig.Unconfigured;
                break;
            case 1:
                config = ElkZoneConfig.Open;
                break;
            case 2:
                config = ElkZoneConfig.EOL;
                break;
            case 3:
                config = ElkZoneConfig.Short;
                break;
        }

        switch ((value >> 2) & 0x03) {
            case 0:
                status = ElkZoneStatus.Normal;
                break;
            case 1:
                status = ElkZoneStatus.Trouble;
                break;
            case 2:
                status = ElkZoneStatus.Violated;
                break;
            case 3:
                status = ElkZoneStatus.Bypassed;
                break;
        }
    }

<<<<<<< HEAD:addons/binding/org.openhab.binding.elkalarm/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
    /** The zone number that has changed. */
    public int getZoneNumber() {
        return zoneNumber;
    }

    /** The current config of the zone. */
=======
>>>>>>> 4550a1098bd36838b8eb6f19bc18146b63e0bb0d:addons/binding/org.openhab.binding.elkm1/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
    public ElkZoneConfig getConfig() {
        return config;
    }

<<<<<<< HEAD:addons/binding/org.openhab.binding.elkalarm/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
    /** The current status of the zone. */
=======
>>>>>>> 4550a1098bd36838b8eb6f19bc18146b63e0bb0d:addons/binding/org.openhab.binding.elkm1/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
    public ElkZoneStatus getStatus() {
        return status;
    }

<<<<<<< HEAD:addons/binding/org.openhab.binding.elkalarm/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
=======
    public int getZoneNumber() {
        return zoneNumber;
    }

>>>>>>> 4550a1098bd36838b8eb6f19bc18146b63e0bb0d:addons/binding/org.openhab.binding.elkm1/src/main/java/org/openhab/binding/elkm1/internal/elk/message/ZoneChangeUpdate.java
    @Override
    protected String getData() {
        // TODO Auto-generated method stub
        return "";
    }

}
