package com.jastigi.silentcampaignmanager.service.simulation.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;

public interface SimulationHistoryService {

    Page<SimulationHistoryResponseDTO> getHistory(
            Pageable pageable);

    Page<SimulationHistoryResponseDTO> getHistoryByPatrol(
            Long patrolId,
            Pageable pageable);

}
