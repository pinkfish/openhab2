package org.openhab.binding.elkm1.internal;

/**
 * The listener for the elk m1 handler.
 *
 * @author David Bennett - Initial Contribution
 */
public interface ElkM1HandlerListener {
    public void onZoneDiscovered(int zoneNum, String label);

    public void onAreaDiscovered(int thingNum, String text);
}
