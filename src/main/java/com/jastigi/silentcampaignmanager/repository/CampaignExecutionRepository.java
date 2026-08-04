package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.CampaignExecution;

public interface CampaignExecutionRepository
        extends JpaRepository<CampaignExecution, Long> {

    @EntityGraph(attributePaths = "campaign")
    List<CampaignExecution> findByCampaignIdOrderByStartedAtDesc(
            Long campaignId);

}
