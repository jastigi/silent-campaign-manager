package com.jastigi.silentcampaignmanager.dto;

import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmarineRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private SubmarineType type;

    @NotNull
    private SubmarineClass submarineClass;

    @NotBlank
    private String nation;

    @NotNull
    private SubmarineStatus status;

    @NotNull
    private SubmarineRole submarineRole;

}
