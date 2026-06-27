package com.jastigi.silentcampaignmanager.dto;

import com.jastigi.silentcampaignmanager.entity.MissionStatus;

import lombok.Data;

@Data
public class PatrolReportDTO {

    /*
     * Patrol
     */

    private Long patrolId;

    private String patrolName;

    /*
     * Campaign
     */

    private Long campaignId;

    private String campaignName;

    /*
     * Submarine
     */

    private Long submarineId;

    private String submarineName;

    private String submarineClass;

    /*
     * Contacts
     */

    private Integer contactsDetected;

    private Integer submarineContacts;

    private Integer surfaceContacts;

    private Integer aircraftContacts;

    private Integer unknownContacts;

    /*
     * Threat Assessment
     */

    private Integer criticalContacts;

    private Integer highThreatContacts;

    /*
     * Intelligence
     */

    private Integer averageConfidence;

    /*
     * Events
     */

    private Integer eventsRecorded;

    private Integer criticalEvents;

    /*
     * Mission Evaluation
     */

    private Integer riskScore;

    private MissionStatus missionStatus;

}
