package com.jastigi.silentcampaignmanager.service.simulation.model;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetectedContact {

    private final ContactType contactType;

    private final Nation nation;

    private final ThreatLevel threatLevel;

    private final int confidenceLevel;

}
