package com.jastigi.silentcampaignmanager.service.simulation.opponent.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;

class OpponentDecisionBehaviourMapperTest {

    private OpponentDecisionBehaviourMapper mapper;

    @BeforeEach
    void setUp() {

        mapper =
                new OpponentDecisionBehaviourMapper();
    }

    @Test
    void shouldMapIgnoreToUnaware() {

        OpponentDecision decision =
                decision(
                        OpponentDecisionType.IGNORE);

        assertEquals(
                ContactBehaviour.UNAWARE,
                mapper.toContactBehaviour(
                        decision));
    }

    @Test
    void shouldMapMonitorToShadowing() {

        OpponentDecision decision =
                decision(
                        OpponentDecisionType.MONITOR);

        assertEquals(
                ContactBehaviour.SHADOWING,
                mapper.toContactBehaviour(
                        decision));
    }

    @Test
    void shouldMapInvestigateToEvasive() {

        OpponentDecision decision =
                decision(
                        OpponentDecisionType.INVESTIGATE);

        assertEquals(
                ContactBehaviour.EVASIVE,
                mapper.toContactBehaviour(
                        decision));
    }

    @Test
    void shouldMapInterceptToAggressive() {

        OpponentDecision decision =
                decision(
                        OpponentDecisionType.INTERCEPT);

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                mapper.toContactBehaviour(
                        decision));
    }

    @Test
    void shouldRejectNullDecision() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                mapper.toContactBehaviour(
                                        null));

        assertEquals(
                "Opponent decision must not be null",
                exception.getMessage());
    }

    private OpponentDecision decision(
            OpponentDecisionType type) {

        return new OpponentDecision(
                type,
                "Test decision rationale");
    }

}
