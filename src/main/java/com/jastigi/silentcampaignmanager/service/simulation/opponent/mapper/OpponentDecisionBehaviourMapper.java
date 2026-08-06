package com.jastigi.silentcampaignmanager.service.simulation.opponent.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;

@Component
public class OpponentDecisionBehaviourMapper {

    public ContactBehaviour toContactBehaviour(
            OpponentDecision decision) {

        if (decision == null) {

            throw new IllegalArgumentException(
                    "Opponent decision must not be null");
        }

        OpponentDecisionType decisionType =
                decision.getType();

        if (decisionType == null) {

            throw new IllegalArgumentException(
                    "Opponent decision type must not be null");
        }

        return switch (decisionType) {

            case IGNORE ->
                ContactBehaviour.UNAWARE;

            case MONITOR ->
                ContactBehaviour.SHADOWING;

            case INVESTIGATE ->
                ContactBehaviour.EVASIVE;

            case INTERCEPT ->
                ContactBehaviour.AGGRESSIVE;
        };
    }

}
