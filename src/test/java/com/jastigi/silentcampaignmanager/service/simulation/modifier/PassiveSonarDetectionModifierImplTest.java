package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.impl.PassiveSonarDetectionModifierImpl;

class PassiveSonarDetectionModifierImplTest {

    private PassiveSonarDetectionModifier modifier;

    @BeforeEach
    void setUp() {

        modifier = new PassiveSonarDetectionModifierImpl();
    }

    @Test
    void shouldApplySsnAndUltraQuietModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.SEAWOLF);

        assertEquals(
                85,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldApplySsnAndQuietModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.LOS_ANGELES);

        assertEquals(
                80,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldApplySsnAndModerateModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.STURGEON);

        assertEquals(
                75,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldApplySsnAndLoudModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.VICTOR_III);

        assertEquals(
                65,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldApplySsbnAndQuietModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.OHIO);

        assertEquals(
                60,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldApplySsbnAndLoudModifiers() {

        Patrol patrol = createPatrol(
                SubmarineClass.TYPHOON);

        assertEquals(
                45,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldKeepProbabilityWhenPatrolIsNull() {

        assertEquals(
                60,
                modifier.apply(
                        null,
                        60));
    }

    @Test
    void shouldKeepProbabilityWhenSubmarineIsMissing() {

        Patrol patrol = Patrol.builder()
                .build();

        assertEquals(
                60,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldKeepProbabilityWhenSubmarineClassIsMissing() {

        Submarine submarine = new Submarine();

        Patrol patrol = Patrol.builder()
                .submarine(submarine)
                .build();

        assertEquals(
                60,
                modifier.apply(
                        patrol,
                        60));
    }

    @Test
    void shouldClampProbabilityToOneHundred() {

        Patrol patrol = createPatrol(
                SubmarineClass.SEAWOLF);

        assertEquals(
                100,
                modifier.apply(
                        patrol,
                        90));
    }

    @Test
    void shouldClampProbabilityToZero() {

        Patrol patrol = createPatrol(
                SubmarineClass.TYPHOON);

        assertEquals(
                0,
                modifier.apply(
                        patrol,
                        5));
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
