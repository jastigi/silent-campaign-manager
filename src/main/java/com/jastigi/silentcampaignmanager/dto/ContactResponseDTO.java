package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;

import lombok.Data;

@Data
public class ContactResponseDTO {

    private Long id;

    private String contactName;

    private ContactType contactType;

    private ThreatLevel threatLevel;

    private LocalDate detectionDate;

    private Long patrolId;

}
