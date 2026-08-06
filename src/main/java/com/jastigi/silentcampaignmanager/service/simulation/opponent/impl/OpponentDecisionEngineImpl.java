package com.jastigi.silentcampaignmanager.service.simulation.opponent.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.OpponentDecisionEngine;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.OpponentDecisionRules;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpponentDecisionEngineImpl
        implements OpponentDecisionEngine {

    private final OpponentDecisionRules
            opponentDecisionRules;

    @Override
    public OpponentDecision decide(
            DetectedContact contact) {

        return opponentDecisionRules.evaluate(
                contact);
    }

}
