package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.PatrolEvent;

public interface PatrolEventRepository extends JpaRepository<PatrolEvent, Long> {

    List<PatrolEvent> findByPatrolId(
            Long patrolId);

}
