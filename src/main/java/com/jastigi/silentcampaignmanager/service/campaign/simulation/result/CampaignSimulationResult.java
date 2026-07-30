package com.jastigi.silentcampaignmanager.service.campaign.simulation.result;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

import lombok.Getter;

@Getter
public final class CampaignSimulationResult {

    private final Campaign campaign;

    private final List<ResolvedSimulationResult> patrolResults;

    private final int totalPatrols;

    private final int completedPatrols;

    private final Instant executedAt;

    public CampaignSimulationResult(
            Campaign campaign,
            List<ResolvedSimulationResult> patrolResults,
            int totalPatrols,
            int completedPatrols,
            Instant executedAt) {

        this.campaign = Objects.requireNonNull(
                campaign,
                "Campaign must not be null");

        this.patrolResults = List.copyOf(
                Objects.requireNonNull(
                        patrolResults,
                        "Patrol results must not be null"));

        if (totalPatrols < 0) {
            throw new IllegalArgumentException(
                    "Total patrols must not be negative");
        }

        if (completedPatrols < 0) {
            throw new IllegalArgumentException(
                    "Completed patrols must not be negative");
        }

        if (completedPatrols > totalPatrols) {
            throw new IllegalArgumentException(
                    "Completed patrols must not exceed total patrols");
        }

        if (this.patrolResults.size() != completedPatrols) {
            throw new IllegalArgumentException(
                    "Patrol result count must match completed patrols");
        }

        this.totalPatrols = totalPatrols;
        this.completedPatrols = completedPatrols;

        this.executedAt = Objects.requireNonNull(
                executedAt,
                "Execution timestamp must not be null");
    }

}
