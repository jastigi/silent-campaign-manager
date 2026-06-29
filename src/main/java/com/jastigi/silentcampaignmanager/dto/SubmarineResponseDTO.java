package com.jastigi.silentcampaignmanager.dto;

import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;

import lombok.Data;

@Data
public class SubmarineResponseDTO {

    private Long id;

    private String name;

    private SubmarineType type;

    private SubmarineClass submarineClass;

    private String nation;

    private SubmarineStatus status;

    private SubmarineRole submarineRole;

}
