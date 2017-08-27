package org.openhab.binding.insteonplm.internal.message;

/**
 * Defines the flags for the all link records.
 *
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
public class AllLinkRecordFlags {
    private final boolean inUse;
    private final boolean isController;
    private final boolean prodData1;
    private final boolean prodData2;
    private final boolean usedBefore;

    public AllLinkRecordFlags(byte data) {
        inUse = (data & 0x80) != 0;
        isController = (data & 0x40) != 0;
        prodData1 = (data & 0x20) != 0;
        prodData2 = (data & 0x10) != 0;
        usedBefore = (data & 0x2) != 0;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean isController() {
        return isController;
    }

    public boolean isProdData1() {
        return prodData1;
    }

    public boolean isProdData2() {
        return prodData2;
    }

    public boolean isUsedBefore() {
        return usedBefore;
    }
}
