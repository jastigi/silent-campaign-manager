package com.jastigi.silentcampaignmanager.service.mission.strategy;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;

public abstract class AbstractMissionStrategy implements MissionStrategy {

    protected int contactCount(Patrol patrol) {

        return patrol.getContacts() == null
                ? 0
                : patrol.getContacts().size();

    }

    protected boolean hasContacts(Patrol patrol) {

        return contactCount(patrol) > 0;

    }

    protected boolean hasMinimumContacts(
            Patrol patrol,
            int minimum) {

        return contactCount(patrol) >= minimum;

    }

    protected int eventCount(Patrol patrol) {

        return patrol.getEvents() == null
                ? 0
                : patrol.getEvents().size();

    }

    protected boolean hasEvents(Patrol patrol) {

        return eventCount(patrol) > 0;

    }

    protected int maximumSeverity(Patrol patrol) {

        if (!hasEvents(patrol)) {
            return 0;
        }

        return patrol.getEvents()
                .stream()
                .mapToInt(PatrolEvent::getSeverity)
                .max()
                .orElse(0);

    }

    protected long countContactsByType(
            Patrol patrol,
            ContactType contactType) {

        if (!hasContacts(patrol)) {
            return 0;
        }

        return patrol.getContacts()
                .stream()
                .filter(contact -> contact.getContactType() == contactType)
                .count();

    }

    protected boolean hasContactType(
            Patrol patrol,
            ContactType contactType) {

        return countContactsByType(
                patrol,
                contactType) > 0;

    }

    protected long submarineContacts(Patrol patrol) {

        return countContactsByType(
                patrol,
                ContactType.SUBMARINE);

    }

    protected long surfaceContacts(Patrol patrol) {

        return countContactsByType(
                patrol,
                ContactType.SURFACE_SHIP);

    }

    protected double averageConfidence(Patrol patrol) {

        if (!hasContacts(patrol)) {
            return 0;
        }

        return patrol.getContacts()
                .stream()
                .mapToInt(Contact::getConfidenceLevel)
                .average()
                .orElse(0);

    }

}
