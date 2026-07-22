package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationResultDTO {

    private String summary;

    private String finalState;

    private LocalDate completionDate;

    private int contactsDetected;

    private int contactsLost;

    private int incidents;

    private List<String> timeline;

}
