package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.impl.SubmarineDetectionModifierImpl;

class SubmarineDetectionModifierImplTest {

    private SubmarineDetectionModifier modifier;

    @BeforeEach
    void setUp() {

        modifier = new SubmarineDetectionModifierImpl();
    }

    @Test
    void shouldAddTenPointsForSsn() {

        Patrol patrol = createPatrol(
                SubmarineClass.LOS_ANGELES);

        assertEquals(
                70,
                modifier.apply(patrol, 60));
    }

    @Test
    void shouldSubtractFifteenPointsForSsbn() {

        Patrol patrol = createPatrol(
                SubmarineClass.OHIO);

        assertEquals(
                45,
                modifier.apply(patrol, 60));
    }

    @Test
    void shouldKeepProbabilityWhenSubmarineIsMissing() {

        Patrol patrol = Patrol.builder()
                .build();

        assertEquals(
                60,
                modifier.apply(patrol, 60));
    }

    @Test
    void shouldKeepProbabilityWhenSubmarineClassIsMissing() {

        Submarine submarine = new Submarine();

        Patrol patrol = Patrol.builder()
                .submarine(submarine)
                .build();

        assertEquals(
                60,
                modifier.apply(patrol, 60));
    }

    private Patrol createPatrol(
            SubmarineClass submarineClass) {

        Submarine submarine = new Submarine();

        submarine.setSubmarineClass(
                submarineClass);

        return Patrol.builder()
                .submarine(submarine)
                .build();
    }

}
