package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByStatus(CampaignStatus status);

}
