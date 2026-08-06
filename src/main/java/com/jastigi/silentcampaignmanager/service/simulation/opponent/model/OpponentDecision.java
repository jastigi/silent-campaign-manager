package com.jastigi.silentcampaignmanager.service.simulation.opponent.model;

import java.util.Objects;

import lombok.Getter;

@Getter
public final class OpponentDecision {

    private final OpponentDecisionType type;

    private final String rationale;

    public OpponentDecision(
            OpponentDecisionType type,
            String rationale) {

        this.type = Objects.requireNonNull(
                type,
                "Opponent decision type must not be null");

        if (rationale == null
                || rationale.isBlank()) {

            throw new IllegalArgumentException(
                    "Opponent decision rationale must not be blank");
        }

        this.rationale = rationale;
    }

}
