package com.jastigi.silentcampaignmanager.service.campaign.simulation.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
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

                CampaignProgress progress = new CampaignProgress(
                                1,
                                1);

                Instant executedAt = Instant.parse(
                                "2026-07-30T09:00:00Z");

                CampaignSimulationResult result = new CampaignSimulationResult(
                                campaign,
                                sourceResults,
                                progress,
                                executedAt);

                sourceResults.clear();

                assertSame(
                                campaign,
                                result.getCampaign());

                assertEquals(
                                List.of(patrolResult),
                                result.getPatrolResults());

                assertSame(
                                progress,
                                result.getProgress());

                assertEquals(
                                executedAt,
                                result.getExecutedAt());

                assertThrows(
                                UnsupportedOperationException.class,
                                () -> result.getPatrolResults().clear());
        }

        @Test
        void shouldRejectNullRequiredValues() {

                Campaign campaign = new Campaign();

                CampaignProgress progress = new CampaignProgress(
                                0,
                                0);

                assertThrows(
                                NullPointerException.class,
                                () -> new CampaignSimulationResult(
                                                null,
                                                List.of(),
                                                progress,
                                                Instant.now()));

                assertThrows(
                                NullPointerException.class,
                                () -> new CampaignSimulationResult(
                                                campaign,
                                                null,
                                                progress,
                                                Instant.now()));

                assertThrows(
                                NullPointerException.class,
                                () -> new CampaignSimulationResult(
                                                campaign,
                                                List.of(),
                                                null,
                                                Instant.now()));

                assertThrows(
                                NullPointerException.class,
                                () -> new CampaignSimulationResult(
                                                campaign,
                                                List.of(),
                                                progress,
                                                null));
        }

}
