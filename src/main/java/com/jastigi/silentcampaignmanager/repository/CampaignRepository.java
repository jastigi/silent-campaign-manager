package com.jastigi.silentcampaignmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

}
