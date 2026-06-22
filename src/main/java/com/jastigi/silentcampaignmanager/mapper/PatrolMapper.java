package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Patrol;

public class PatrolMapper {

    public static Patrol toEntity(
            PatrolRequestDTO dto) {

        Patrol patrol = new Patrol();

        patrol.setPatrolName(dto.getPatrolName());
        patrol.setPatrolDate(dto.getPatrolDate());
        patrol.setArea(dto.getArea());
        patrol.setResult(dto.getResult());

        return patrol;
    }

    public static PatrolResponseDTO toDTO(
            Patrol patrol) {

        PatrolResponseDTO dto = new PatrolResponseDTO();

        dto.setId(patrol.getId());
        dto.setPatrolName(patrol.getPatrolName());
        dto.setPatrolDate(patrol.getPatrolDate());
        dto.setArea(patrol.getArea());
        dto.setResult(patrol.getResult());

        if (patrol.getCampaign() != null) {
            dto.setCampaignId(
                    patrol.getCampaign().getId());
        }

        return dto;
    }

}
