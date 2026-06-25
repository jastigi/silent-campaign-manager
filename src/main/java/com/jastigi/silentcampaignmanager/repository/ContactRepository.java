package com.jastigi.silentcampaignmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jastigi.silentcampaignmanager.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByPatrolId(Long patrolId);

}
