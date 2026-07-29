package com.jastigi.silentcampaignmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

public interface SimulationRecordRepository
        extends JpaRepository<SimulationRecord, Long> {

}
