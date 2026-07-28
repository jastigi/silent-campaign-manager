package com.jastigi.silentcampaignmanager.entity;

public enum SubmarineClass {

    OHIO(
            SubmarineRole.SSBN,
            AcousticSignature.QUIET),

    LOS_ANGELES(
            SubmarineRole.SSN,
            AcousticSignature.QUIET),

    STURGEON(
            SubmarineRole.SSN,
            AcousticSignature.MODERATE),

    SEAWOLF(
            SubmarineRole.SSN,
            AcousticSignature.ULTRA_QUIET),

    RESOLUTION(
            SubmarineRole.SSBN,
            AcousticSignature.MODERATE),

    SWIFTSURE(
            SubmarineRole.SSN,
            AcousticSignature.MODERATE),

    TRAFALGAR(
            SubmarineRole.SSN,
            AcousticSignature.QUIET),

    DELTA_IV(
            SubmarineRole.SSBN,
            AcousticSignature.MODERATE),

    TYPHOON(
            SubmarineRole.SSBN,
            AcousticSignature.LOUD),

    VICTOR_III(
            SubmarineRole.SSN,
            AcousticSignature.LOUD),

    AKULA(
            SubmarineRole.SSN,
            AcousticSignature.QUIET);

    private final SubmarineRole role;

    private final AcousticSignature acousticSignature;

    SubmarineClass(
            SubmarineRole role,
            AcousticSignature acousticSignature) {

        this.role = role;
        this.acousticSignature = acousticSignature;
    }

    public SubmarineRole getRole() {

        return role;
    }

    public AcousticSignature getAcousticSignature() {

        return acousticSignature;
    }

}
