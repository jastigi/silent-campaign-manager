package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface PatrolRepository extends JpaRepository<Patrol, Long> {

    List<Patrol> findByCampaignId(Long campaignId);

}
