package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.DeterrencePatrolSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.EscortSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.FollowSsbnSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.HuntSsnSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.IntelligenceSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.SpecialOperationSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.SurveillanceSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.TrainingSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class MissionSuccessStrategiesTest {

    @Test
    void deterrenceShouldSucceedWhenPatrolIsCompleted() {

        MissionSuccessStrategy strategy = new DeterrencePatrolSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        completedResult()));
    }

    @Test
    void deterrenceShouldFailWhenPatrolIsNotCompleted() {

        MissionSuccessStrategy strategy = new DeterrencePatrolSuccessStrategy();

        SimulationResult result = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.TRANSIT)
                .build();

        assertEquals(
                MissionOutcome.FAILURE,
                strategy.resolve(result));
    }

    @Test
    void huntSsnShouldSucceedWithClassifiedSubmarine() {

        MissionSuccessStrategy strategy = new HuntSsnSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SUBMARINE,
                                ContactClassificationStatus.CLASSIFIED,
                                80)));
    }

    @Test
    void huntSsnShouldBePartialWithUnclassifiedSubmarine() {

        MissionSuccessStrategy strategy = new HuntSsnSuccessStrategy();

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SUBMARINE,
                                ContactClassificationStatus.UNCLASSIFIED,
                                60)));
    }

    @Test
    void huntSsnShouldFailWithoutSubmarine() {

        MissionSuccessStrategy strategy = new HuntSsnSuccessStrategy();

        assertEquals(
                MissionOutcome.FAILURE,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SURFACE_SHIP,
                                ContactClassificationStatus.CLASSIFIED,
                                90)));
    }

    @Test
    void followSsbnShouldSucceedWithClassifiedSubmarine() {

        MissionSuccessStrategy strategy = new FollowSsbnSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SUBMARINE,
                                ContactClassificationStatus.CLASSIFIED,
                                75)));
    }

    @Test
    void surveillanceShouldSucceedWithClassifiedContact() {

        MissionSuccessStrategy strategy = new SurveillanceSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SURFACE_SHIP,
                                ContactClassificationStatus.CLASSIFIED,
                                70)));
    }

    @Test
    void surveillanceShouldBePartialWithUnclassifiedContact() {

        MissionSuccessStrategy strategy = new SurveillanceSuccessStrategy();

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SURFACE_SHIP,
                                ContactClassificationStatus.UNCLASSIFIED,
                                50)));
    }

    @Test
    void intelligenceShouldSucceedWithUsefulIntelligence() {

        MissionSuccessStrategy strategy = new IntelligenceSuccessStrategy();

        DetectedContact contact = DetectedContact.builder()
                .contactType(ContactType.SURFACE_SHIP)
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .confidenceLevel(80)
                .intelligenceGathered(true)
                .build();

        SimulationResult result = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .contactsDetected(1)
                .detectedContacts(
                        List.of(contact))
                .build();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(result));
    }

    @Test
    void intelligenceShouldBePartialWhenConfidenceIsTooLow() {

        MissionSuccessStrategy strategy = new IntelligenceSuccessStrategy();

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                strategy.resolve(
                        resultWithContact(
                                ContactType.SURFACE_SHIP,
                                ContactClassificationStatus.CLASSIFIED,
                                55)));
    }

    @Test
    void specialOperationShouldSucceedWithoutIncidents() {

        MissionSuccessStrategy strategy = new SpecialOperationSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        completedResult()));
    }

    @Test
    void specialOperationShouldBePartialWithIncidents() {

        MissionSuccessStrategy strategy = new SpecialOperationSuccessStrategy();

        SimulationResult result = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .incidents(1)
                .build();

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                strategy.resolve(result));
    }

    @Test
    void escortShouldSucceedWithoutIncidents() {

        MissionSuccessStrategy strategy = new EscortSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        completedResult()));
    }

    @Test
    void escortShouldBePartialWithIncidents() {

        MissionSuccessStrategy strategy = new EscortSuccessStrategy();

        SimulationResult result = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .incidents(2)
                .build();

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                strategy.resolve(result));
    }

    @Test
    void trainingShouldSucceedWhenCompleted() {

        MissionSuccessStrategy strategy = new TrainingSuccessStrategy();

        assertEquals(
                MissionOutcome.SUCCESS,
                strategy.resolve(
                        completedResult()));
    }

    @Test
    void trainingShouldFailWhenNotCompleted() {

        MissionSuccessStrategy strategy = new TrainingSuccessStrategy();

        SimulationResult result = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.RETURNING)
                .build();

        assertEquals(
                MissionOutcome.FAILURE,
                strategy.resolve(result));
    }

    private SimulationResult completedResult() {

        return SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .incidents(0)
                .detectedContacts(List.of())
                .build();
    }

    private SimulationResult resultWithContact(
            ContactType contactType,
            ContactClassificationStatus classificationStatus,
            int confidenceLevel) {

        DetectedContact contact = DetectedContact.builder()
                .contactType(contactType)
                .classificationStatus(
                        classificationStatus)
                .confidenceLevel(
                        confidenceLevel)
                .build();

        return SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .contactsDetected(1)
                .detectedContacts(
                        List.of(contact))
                .build();
    }

}
