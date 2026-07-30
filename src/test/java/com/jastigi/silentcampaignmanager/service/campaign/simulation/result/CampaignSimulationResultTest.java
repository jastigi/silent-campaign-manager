package com.jastigi.silentcampaignmanager.service.campaign.simulation.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

class CampaignSimulationResultTest {

    @Test
    void shouldCreateImmutableCampaignSimulationResult() {

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("North Atlantic Campaign");

        ResolvedSimulationResult patrolResult = ResolvedSimulationResult.builder()
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .missionScore(90)
                .build();

        List<ResolvedSimulationResult> sourceResults = new ArrayList<>();
        sourceResults.add(
                patrolResult);

        Instant executedAt = Instant.parse(
                "2026-07-29T10:00:00Z");

        CampaignSimulationResult result = new CampaignSimulationResult(
                campaign,
                sourceResults,
                1,
                1,
                executedAt);

        sourceResults.clear();

        assertSame(
                campaign,
                result.getCampaign());

        assertEquals(
                List.of(patrolResult),
                result.getPatrolResults());

        assertEquals(
                1,
                result.getTotalPatrols());

        assertEquals(
                1,
                result.getCompletedPatrols());

        assertEquals(
                executedAt,
                result.getExecutedAt());

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getPatrolResults().clear());
    }

    @Test
    void shouldRejectCompletedPatrolCountGreaterThanTotal() {

        Campaign campaign = new Campaign();

        assertThrows(
                IllegalArgumentException.class,
                () -> new CampaignSimulationResult(
                        campaign,
                        List.of(),
                        1,
                        2,
                        Instant.now()));
    }

    @Test
    void shouldRejectResultCountDifferentFromCompletedPatrols() {

        Campaign campaign = new Campaign();

        ResolvedSimulationResult patrolResult = ResolvedSimulationResult.builder()
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> new CampaignSimulationResult(
                        campaign,
                        List.of(patrolResult),
                        2,
                        2,
                        Instant.now()));
    }

    @Test
    void shouldRejectNullRequiredValues() {

        Campaign campaign = new Campaign();

        assertThrows(
                NullPointerException.class,
                () -> new CampaignSimulationResult(
                        null,
                        List.of(),
                        0,
                        0,
                        Instant.now()));

        assertThrows(
                NullPointerException.class,
                () -> new CampaignSimulationResult(
                        campaign,
                        null,
                        0,
                        0,
                        Instant.now()));

        assertThrows(
                NullPointerException.class,
                () -> new CampaignSimulationResult(
                        campaign,
                        List.of(),
                        0,
                        0,
                        null));
    }

}
