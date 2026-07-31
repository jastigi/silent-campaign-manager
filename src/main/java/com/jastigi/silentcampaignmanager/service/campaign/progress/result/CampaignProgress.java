package com.jastigi.silentcampaignmanager.service.campaign.progress.result;

import lombok.Getter;

@Getter
public final class CampaignProgress {

    private final int totalPatrols;

    private final int completedPatrols;

    private final int pendingPatrols;

    private final double completionPercentage;

    private final boolean completed;

    public CampaignProgress(
            int totalPatrols,
            int completedPatrols) {

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

        this.totalPatrols = totalPatrols;
        this.completedPatrols = completedPatrols;
        this.pendingPatrols = totalPatrols - completedPatrols;

        this.completionPercentage = calculateCompletionPercentage(
                totalPatrols,
                completedPatrols);

        this.completed = totalPatrols > 0
                && completedPatrols == totalPatrols;
    }

    private double calculateCompletionPercentage(
            int totalPatrols,
            int completedPatrols) {

        if (totalPatrols == 0) {
            return 0.0;
        }

        double percentage = completedPatrols * 100.0 / totalPatrols;

        return Math.round(percentage * 100.0) / 100.0;
    }

}
