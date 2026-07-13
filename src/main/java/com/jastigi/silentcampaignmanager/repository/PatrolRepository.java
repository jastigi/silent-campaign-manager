package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

public interface PatrolRepository extends JpaRepository<Patrol, Long> {

    List<Patrol> findByCampaignId(Long campaignId);

    Page<Patrol> findByCampaignId(Long campaignId, Pageable pageable);

    Page<Patrol> findByCampaignIdAndResult(
            Long campaignId,
            PatrolResult result,
            Pageable pageable);

}
