package com.jastigi.silentcampaignmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

public interface SimulationRecordRepository
                extends JpaRepository<SimulationRecord, Long> {

        @Override
        @EntityGraph(attributePaths = "patrol")
        Page<SimulationRecord> findAll(
                        Pageable pageable);

        @EntityGraph(attributePaths = "patrol")
        Page<SimulationRecord> findByPatrolId(
                        Long patrolId,
                        Pageable pageable);

}
