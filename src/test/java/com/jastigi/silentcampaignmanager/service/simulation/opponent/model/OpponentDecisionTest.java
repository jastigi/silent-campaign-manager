package com.jastigi.silentcampaignmanager.service.simulation.opponent.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OpponentDecisionTest {

    @Test
    void shouldCreateOpponentDecision() {

        OpponentDecision decision = new OpponentDecision(
                OpponentDecisionType.INTERCEPT,
                "Critical submarine contact classified with high confidence");

        assertEquals(
                OpponentDecisionType.INTERCEPT,
                decision.getType());

        assertEquals(
                "Critical submarine contact classified with high confidence",
                decision.getRationale());
    }

    @Test
    void shouldRejectNullDecisionType() {

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new OpponentDecision(
                        null,
                        "Valid rationale"));

        assertEquals(
                "Opponent decision type must not be null",
                exception.getMessage());
    }

    @Test
    void shouldRejectNullRationale() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OpponentDecision(
                        OpponentDecisionType.MONITOR,
                        null));

        assertEquals(
                "Opponent decision rationale must not be blank",
                exception.getMessage());
    }

    @Test
    void shouldRejectBlankRationale() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OpponentDecision(
                        OpponentDecisionType.MONITOR,
                        "   "));

        assertEquals(
                "Opponent decision rationale must not be blank",
                exception.getMessage());
    }

}
