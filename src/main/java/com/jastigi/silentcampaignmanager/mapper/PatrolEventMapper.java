package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;

public class PatrolEventMapper {

    private PatrolEventMapper() {

    }

    public static PatrolEvent toEntity(
            PatrolEventRequestDTO dto) {

        PatrolEvent event = new PatrolEvent();

        event.setEventType(
                dto.getEventType());

        event.setEventDate(
                dto.getEventDate());

        event.setDescription(
                dto.getDescription());

        event.setSeverity(
                dto.getSeverity());

        return event;
    }

    public static PatrolEventResponseDTO toDTO(
            PatrolEvent event) {

        PatrolEventResponseDTO dto = new PatrolEventResponseDTO();

        dto.setId(event.getId());

        dto.setEventType(
                event.getEventType());

        dto.setEventDate(
                event.getEventDate());

        dto.setDescription(
                event.getDescription());

        dto.setSeverity(
                event.getSeverity());

        if (event.getPatrol() != null) {
            dto.setPatrolId(
                    event.getPatrol().getId());
        }

        return dto;
    }

}
