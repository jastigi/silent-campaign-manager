package com.jastigi.silentcampaignmanager.entity;

public enum SubmarineClass {

    OHIO(SubmarineRole.SSBN),

    LOS_ANGELES(SubmarineRole.SSN),

    STURGEON(SubmarineRole.SSN),

    SEAWOLF(SubmarineRole.SSN),

    RESOLUTION(SubmarineRole.SSBN),

    SWIFTSURE(SubmarineRole.SSN),

    TRAFALGAR(SubmarineRole.SSN),

    DELTA_IV(SubmarineRole.SSBN),

    TYPHOON(SubmarineRole.SSBN),

    VICTOR_III(SubmarineRole.SSN),

    AKULA(SubmarineRole.SSN);

    private final SubmarineRole role;

    SubmarineClass(SubmarineRole role) {
        this.role = role;
    }

    public SubmarineRole getRole() {
        return role;
    }

}
