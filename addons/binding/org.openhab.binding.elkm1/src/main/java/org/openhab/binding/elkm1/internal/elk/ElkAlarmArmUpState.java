package org.openhab.binding.elkm1.internal.elk;

public enum ElkAlarmArmUpState {
    NotReadyToArm,
    ReadyToArm,
    ReadyToArmButZoneIsViolated,
    ArmedWithExitTimerWorking,
    ArmedFully,
    ForceArmedWithAForceArmZoneViolated,
    ArmedWithABypass
}
