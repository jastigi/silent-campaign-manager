package com.jastigi.silentcampaignmanager.service.simulation.opponent.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.OpponentDecisionEngine;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.OpponentDecisionRules;

@ExtendWith(MockitoExtension.class)
class OpponentDecisionEngineImplTest {

    @Mock
    private OpponentDecisionRules
            opponentDecisionRules;

    private OpponentDecisionEngine
            opponentDecisionEngine;

    @BeforeEach
    void setUp() {

        opponentDecisionEngine =
                new OpponentDecisionEngineImpl(
                        opponentDecisionRules);
    }

    @Test
    void shouldDelegateDecisionToRules() {

        DetectedContact contact =
                DetectedContact.builder()
                        .contactType(
                                ContactType.SUBMARINE)
                        .threatLevel(
                                ThreatLevel.HIGH)
                        .confidenceLevel(85)
                        .build();

        OpponentDecision decision =
                new OpponentDecision(
                        OpponentDecisionType.INTERCEPT,
                        "High-threat contact identified with high confidence");

        when(
                opponentDecisionRules.evaluate(
                        contact))
                .thenReturn(
                        decision);

        OpponentDecision result =
                opponentDecisionEngine.decide(
                        contact);

        assertSame(
                decision,
                result);

        verify(
                opponentDecisionRules)
                .evaluate(
                        contact);
    }

}
