package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public interface CampaignRepository extends JpaRepository<Campaign, Long>,
        JpaSpecificationExecutor<Campaign> {

    List<Campaign> findByStatus(CampaignStatus status);

    Page<Campaign> findByStatus(
            CampaignStatus status,
            Pageable pageable);

}
