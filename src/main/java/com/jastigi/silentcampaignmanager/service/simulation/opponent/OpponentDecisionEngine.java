package com.jastigi.silentcampaignmanager.service.simulation.opponent;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;

public interface OpponentDecisionEngine {

    OpponentDecision decide(
            DetectedContact contact);

}
