package com.jastigi.silentcampaignmanager.service.simulation.result;

import java.time.LocalDate;
import java.util.List;

import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationResult {

    private final boolean success;

    private final String summary;

    private final List<String> eventLog;

    private final int contactsDetected;

    private final int contactsLost;

    private final int incidents;

    private final PatrolSimulationState finalState;

    private final List<DetectedContact> detectedContacts;

    private final LocalDate completionDate;

}
