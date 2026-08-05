package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;

public interface SubmarineRepository
        extends JpaRepository<Submarine, Long> {

    List<Submarine> findByStatus(
            SubmarineStatus status);

}
