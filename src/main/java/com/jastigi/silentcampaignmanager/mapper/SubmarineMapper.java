package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Submarine;

public class SubmarineMapper {

    private SubmarineMapper() {
    }

    public static Submarine toEntity(
            SubmarineRequestDTO dto) {

        Submarine submarine = new Submarine();

        submarine.setName(dto.getName());
        submarine.setSubmarineType(dto.getType());
        submarine.setSubmarineClass(
                dto.getSubmarineClass());
        submarine.setNation(dto.getNation());
        submarine.setStatus(dto.getStatus());
        submarine.setSubmarineRole(dto.getSubmarineRole());

        return submarine;
    }

    public static SubmarineResponseDTO toDTO(
            Submarine submarine) {

        SubmarineResponseDTO dto = new SubmarineResponseDTO();

        dto.setId(submarine.getId());
        dto.setName(submarine.getName());
        dto.setType(submarine.getSubmarineType());
        dto.setSubmarineClass(submarine.getSubmarineClass());
        dto.setNation(submarine.getNation());
        dto.setStatus(submarine.getStatus());
        dto.setSubmarineRole(submarine.getSubmarineRole());

        return dto;
    }

}
