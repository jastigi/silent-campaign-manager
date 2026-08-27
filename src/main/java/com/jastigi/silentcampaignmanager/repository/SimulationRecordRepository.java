package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

        boolean existsByPatrolId(Long patrolId);

        List<SimulationRecord> findByPatrolCampaignId(
                        Long campaignId);

        @Query("""
                        select count(distinct simulationRecord.patrol.id)
                        from SimulationRecord simulationRecord
                        where simulationRecord.patrol.campaign.id = :campaignId
                        """)
        long countDistinctSimulatedPatrolsByCampaignId(
                        @Param("campaignId") Long campaignId);

}
