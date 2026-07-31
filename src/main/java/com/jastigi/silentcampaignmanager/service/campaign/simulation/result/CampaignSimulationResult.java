package com.jastigi.silentcampaignmanager.service.campaign.simulation.result;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

import lombok.Getter;

@Getter
public final class CampaignSimulationResult {

        private final Campaign campaign;

        private final List<ResolvedSimulationResult> patrolResults;

        private final CampaignProgress progress;

        private final Instant executedAt;

        public CampaignSimulationResult(
                        Campaign campaign,
                        List<ResolvedSimulationResult> patrolResults,
                        CampaignProgress progress,
                        Instant executedAt) {

                this.campaign = Objects.requireNonNull(
                                campaign,
                                "Campaign must not be null");

                this.patrolResults = List.copyOf(
                                Objects.requireNonNull(
                                                patrolResults,
                                                "Patrol results must not be null"));

                this.progress = Objects.requireNonNull(
                                progress,
                                "Campaign progress must not be null");

                this.executedAt = Objects.requireNonNull(
                                executedAt,
                                "Execution timestamp must not be null");
        }

}
