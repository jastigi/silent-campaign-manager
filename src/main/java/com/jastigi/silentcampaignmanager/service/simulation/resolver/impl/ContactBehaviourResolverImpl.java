package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.OpponentDecisionEngine;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.mapper.OpponentDecisionBehaviourMapper;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactBehaviourResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContactBehaviourResolverImpl
        implements ContactBehaviourResolver {

    private final OpponentDecisionEngine
            opponentDecisionEngine;

    private final OpponentDecisionBehaviourMapper
            opponentDecisionBehaviourMapper;

    @Override
    public ContactBehaviour resolve(
            DetectedContact contact) {

        if (contact == null) {

            return ContactBehaviour.UNAWARE;
        }

        OpponentDecision decision =
                opponentDecisionEngine.decide(
                        contact);

        return opponentDecisionBehaviourMapper
                .toContactBehaviour(
                        decision);
    }

}
