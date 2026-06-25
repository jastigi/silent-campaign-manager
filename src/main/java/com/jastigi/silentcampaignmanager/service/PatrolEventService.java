package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;

public interface PatrolEventService {

    PatrolEventResponseDTO createEvent(
            Long patrolId,
            PatrolEventRequestDTO request);

    List<PatrolEventResponseDTO> getEventsByPatrol(
            Long patrolId);

    PatrolEventResponseDTO getEventById(
            Long id);

    PatrolEventResponseDTO updateEvent(
            Long id,
            PatrolEventRequestDTO request);

    void deleteEvent(
            Long id);

}
