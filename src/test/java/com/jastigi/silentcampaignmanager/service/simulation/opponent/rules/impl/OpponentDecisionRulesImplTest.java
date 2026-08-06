package com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.OpponentDecisionRules;

class OpponentDecisionRulesImplTest {

    private OpponentDecisionRules
            opponentDecisionRules;

    @BeforeEach
    void setUp() {

        opponentDecisionRules =
                new OpponentDecisionRulesImpl();
    }

    @Test
    void shouldInterceptCriticalThreat() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.UNKNOWN,
                                ThreatLevel.CRITICAL,
                                20));

        assertEquals(
                OpponentDecisionType.INTERCEPT,
                decision.getType());

        assertEquals(
                "Critical threat requires immediate interception",
                decision.getRationale());
    }

    @Test
    void shouldInterceptHighThreatWithHighConfidence() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.SURFACE_SHIP,
                                ThreatLevel.HIGH,
                                80));

        assertEquals(
                OpponentDecisionType.INTERCEPT,
                decision.getType());
    }

    @Test
    void shouldInvestigateHighThreatWithLowerConfidence() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.UNKNOWN,
                                ThreatLevel.HIGH,
                                60));

        assertEquals(
                OpponentDecisionType.INVESTIGATE,
                decision.getType());
    }

    @Test
    void shouldInterceptConfirmedMediumThreatSubmarine() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.SUBMARINE,
                                ThreatLevel.MEDIUM,
                                75));

        assertEquals(
                OpponentDecisionType.INTERCEPT,
                decision.getType());
    }

    @Test
    void shouldInvestigateMediumThreatWithSufficientConfidence() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.AIRCRAFT,
                                ThreatLevel.MEDIUM,
                                60));

        assertEquals(
                OpponentDecisionType.INVESTIGATE,
                decision.getType());
    }

    @Test
    void shouldMonitorMediumThreatWithLowConfidence() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.UNKNOWN,
                                ThreatLevel.MEDIUM,
                                35));

        assertEquals(
                OpponentDecisionType.MONITOR,
                decision.getType());
    }

    @Test
    void shouldMonitorConfirmedLowThreatSubmarine() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.SUBMARINE,
                                ThreatLevel.LOW,
                                80));

        assertEquals(
                OpponentDecisionType.MONITOR,
                decision.getType());
    }

    @Test
    void shouldIgnoreLowThreatContact() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.UNKNOWN,
                                ThreatLevel.LOW,
                                30));

        assertEquals(
                OpponentDecisionType.IGNORE,
                decision.getType());
    }

    @Test
    void shouldNormalizeConfidenceAboveOneHundred() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.SURFACE_SHIP,
                                ThreatLevel.HIGH,
                                150));

        assertEquals(
                OpponentDecisionType.INTERCEPT,
                decision.getType());
    }

    @Test
    void shouldNormalizeNegativeConfidence() {

        OpponentDecision decision =
                opponentDecisionRules.evaluate(
                        contact(
                                ContactType.UNKNOWN,
                                ThreatLevel.MEDIUM,
                                -10));

        assertEquals(
                OpponentDecisionType.MONITOR,
                decision.getType());
    }

    @Test
    void shouldRejectNullContact() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                opponentDecisionRules.evaluate(
                                        null));

        assertEquals(
                "Detected contact must not be null",
                exception.getMessage());
    }

    @Test
    void shouldRejectContactWithoutThreatLevel() {

        DetectedContact contact =
                DetectedContact.builder()
                        .contactType(
                                ContactType.SUBMARINE)
                        .confidenceLevel(80)
                        .build();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                opponentDecisionRules.evaluate(
                                        contact));

        assertEquals(
                "Detected contact threat level must not be null",
                exception.getMessage());
    }

    @Test
    void shouldRejectContactWithoutContactType() {

        DetectedContact contact =
                DetectedContact.builder()
                        .threatLevel(
                                ThreatLevel.HIGH)
                        .confidenceLevel(80)
                        .build();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                opponentDecisionRules.evaluate(
                                        contact));

        assertEquals(
                "Detected contact type must not be null",
                exception.getMessage());
    }

    private DetectedContact contact(
            ContactType contactType,
            ThreatLevel threatLevel,
            int confidenceLevel) {

        return DetectedContact.builder()
                .contactType(
                        contactType)
                .threatLevel(
                        threatLevel)
                .confidenceLevel(
                        confidenceLevel)
                .build();
    }

}
