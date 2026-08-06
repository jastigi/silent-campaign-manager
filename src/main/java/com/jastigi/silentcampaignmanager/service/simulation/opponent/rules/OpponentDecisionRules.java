package com.jastigi.silentcampaignmanager.service.simulation.opponent.rules;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;

public interface OpponentDecisionRules {

    OpponentDecision evaluate(
            DetectedContact contact);

}
