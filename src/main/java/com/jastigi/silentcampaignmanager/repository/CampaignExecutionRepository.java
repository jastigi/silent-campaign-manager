package com.jastigi.silentcampaignmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.CampaignExecution;

public interface CampaignExecutionRepository
                extends JpaRepository<CampaignExecution, Long> {

        @EntityGraph(attributePaths = "campaign")
        Page<CampaignExecution> findByCampaignId(
                        Long campaignId,
                        Pageable pageable);

}
