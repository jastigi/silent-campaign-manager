package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.OpponentDecisionEngine;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.mapper.OpponentDecisionBehaviourMapper;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.ContactBehaviourResolverImpl;

@ExtendWith(MockitoExtension.class)
class ContactBehaviourResolverImplTest {

    @Mock
    private OpponentDecisionEngine
            opponentDecisionEngine;

    @Mock
    private OpponentDecisionBehaviourMapper
            opponentDecisionBehaviourMapper;

    private ContactBehaviourResolver resolver;

    @BeforeEach
    void setUp() {

        resolver =
                new ContactBehaviourResolverImpl(
                        opponentDecisionEngine,
                        opponentDecisionBehaviourMapper);
    }

    @Test
    void shouldReturnUnawareForNullContact() {

        ContactBehaviour result =
                resolver.resolve(
                        null);

        assertEquals(
                ContactBehaviour.UNAWARE,
                result);

        verify(
                opponentDecisionEngine,
                never())
                .decide(
                        org.mockito.ArgumentMatchers.any());

        verify(
                opponentDecisionBehaviourMapper,
                never())
                .toContactBehaviour(
                        org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldResolveContactBehaviourFromOpponentDecision() {

        DetectedContact contact =
                contact();

        OpponentDecision decision =
                new OpponentDecision(
                        OpponentDecisionType.INTERCEPT,
                        "High-threat contact identified with high confidence");

        when(
                opponentDecisionEngine.decide(
                        contact))
                .thenReturn(
                        decision);

        when(
                opponentDecisionBehaviourMapper
                        .toContactBehaviour(
                                decision))
                .thenReturn(
                        ContactBehaviour.AGGRESSIVE);

        ContactBehaviour result =
                resolver.resolve(
                        contact);

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                result);

        verify(
                opponentDecisionEngine)
                .decide(
                        contact);

        verify(
                opponentDecisionBehaviourMapper)
                .toContactBehaviour(
                        decision);
    }

    @Test
    void shouldReturnMappedMonitorBehaviour() {

        DetectedContact contact =
                contact();

        OpponentDecision decision =
                new OpponentDecision(
                        OpponentDecisionType.MONITOR,
                        "Medium-threat contact remains under observation");

        when(
                opponentDecisionEngine.decide(
                        contact))
                .thenReturn(
                        decision);

        when(
                opponentDecisionBehaviourMapper
                        .toContactBehaviour(
                                decision))
                .thenReturn(
                        ContactBehaviour.SHADOWING);

        ContactBehaviour result =
                resolver.resolve(
                        contact);

        assertEquals(
                ContactBehaviour.SHADOWING,
                result);
    }

    private DetectedContact contact() {

        return DetectedContact.builder()
                .contactType(
                        ContactType.SUBMARINE)
                .threatLevel(
                        ThreatLevel.HIGH)
                .confidenceLevel(85)
                .build();
    }

}
