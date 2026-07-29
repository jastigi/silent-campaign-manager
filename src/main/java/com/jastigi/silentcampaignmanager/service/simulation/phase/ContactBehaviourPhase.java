package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactBehaviourResolver;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ShadowingDecisionResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(5)
public class ContactBehaviourPhase
                implements SimulationPhase {

        private final ContactBehaviourResolver contactBehaviourResolver;
        private final ShadowingDecisionResolver shadowingDecisionResolver;

        @Override
        public void execute(
                        SimulationContext context) {

                if (context.getDetectedContacts() == null
                                || context.getDetectedContacts().isEmpty()) {

                        return;
                }

                for (DetectedContact contact : context.getDetectedContacts()) {

                        ContactBehaviour behaviour = contactBehaviourResolver.resolve(
                                        contact);

                        contact.setBehaviour(behaviour);

                        context.addEvent(
                                        SimulationEventType.CONTACT_BEHAVIOUR_RESOLVED,
                                        "Contact behaviour resolved as "
                                                        + behaviour
                                                        + " for "
                                                        + contact.getContactType()
                                                        + ".");

                        boolean shadowing = shadowingDecisionResolver.shouldShadow(
                                        context.getPatrol(),
                                        contact);

                        contact.setShadowing(
                                        shadowing);

                        context.addEvent(
                                        SimulationEventType.SHADOWING_DECISION,
                                        shadowing
                                                        ? "Shadowing initiated."
                                                        : "Shadowing not initiated.");
                }
        }
}
