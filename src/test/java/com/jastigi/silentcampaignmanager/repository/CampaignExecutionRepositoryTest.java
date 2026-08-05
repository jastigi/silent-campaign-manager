package com.jastigi.silentcampaignmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

import jakarta.persistence.EntityManager;

@DataJpaTest
class CampaignExecutionRepositoryTest {

        @Autowired
        private CampaignExecutionRepository campaignExecutionRepository;

        @Autowired
        private EntityManager entityManager;

        @Test
        void shouldPersistCampaignExecution() {

                Campaign campaign = persistCampaign(
                                "North Atlantic Campaign");

                CampaignExecution execution = CampaignExecution.builder()
                                .campaign(
                                                campaign)
                                .status(
                                                CampaignExecutionStatus.RUNNING)
                                .totalPatrols(3)
                                .completedPatrols(0)
                                .startedAt(
                                                LocalDateTime.of(
                                                                2026,
                                                                8,
                                                                4,
                                                                10,
                                                                0))
                                .build();

                CampaignExecution saved = campaignExecutionRepository
                                .saveAndFlush(
                                                execution);

                assertEquals(
                                CampaignExecutionStatus.RUNNING,
                                saved.getStatus());

                assertEquals(
                                3,
                                saved.getTotalPatrols());

                assertEquals(
                                campaign.getId(),
                                saved.getCampaign()
                                                .getId());
        }

        @Test
        void shouldFindPagedExecutionsByCampaignOrderedByStartDescending() {

                Campaign firstCampaign = persistCampaign(
                                "First Campaign");

                Campaign secondCampaign = persistCampaign(
                                "Second Campaign");

                persistExecution(
                                firstCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                4,
                                                10,
                                                0));

                persistExecution(
                                firstCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                4,
                                                12,
                                                0));

                persistExecution(
                                secondCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                4,
                                                14,
                                                0));

                PageRequest pageable = PageRequest.of(
                                0,
                                10,
                                Sort.by(
                                                Sort.Direction.DESC,
                                                "startedAt"));

                Page<CampaignExecution> executions = campaignExecutionRepository
                                .findByCampaignId(
                                                firstCampaign.getId(),
                                                pageable);

                assertEquals(
                                2,
                                executions.getTotalElements());

                assertEquals(
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                4,
                                                12,
                                                0),
                                executions.getContent()
                                                .get(0)
                                                .getStartedAt());

                assertEquals(
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                4,
                                                10,
                                                0),
                                executions.getContent()
                                                .get(1)
                                                .getStartedAt());

                assertEquals(
                                firstCampaign.getId(),
                                executions.getContent()
                                                .get(0)
                                                .getCampaign()
                                                .getId());
        }

        @Test
        void shouldFindAllExecutionsByCampaign() {

                Campaign firstCampaign = persistCampaign(
                                "Timeline Campaign");

                Campaign secondCampaign = persistCampaign(
                                "Other Campaign");

                persistExecution(
                                firstCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                5,
                                                10,
                                                0));

                persistExecution(
                                firstCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                5,
                                                11,
                                                0));

                persistExecution(
                                secondCampaign,
                                LocalDateTime.of(
                                                2026,
                                                8,
                                                5,
                                                12,
                                                0));

                List<CampaignExecution> executions = campaignExecutionRepository.findByCampaignId(
                                firstCampaign.getId());

                assertEquals(
                                2,
                                executions.size());

                assertEquals(
                                firstCampaign.getId(),
                                executions.get(0)
                                                .getCampaign()
                                                .getId());

                assertEquals(
                                firstCampaign.getId(),
                                executions.get(1)
                                                .getCampaign()
                                                .getId());
        }

        private Campaign persistCampaign(
                        String name) {

                Campaign campaign = new Campaign();

                campaign.setName(
                                name);

                campaign.setDescription(
                                "Repository test campaign");

                campaign.setStartDate(
                                LocalDate.of(
                                                1985,
                                                1,
                                                1));

                campaign.setStatus(
                                CampaignStatus.ACTIVE);

                entityManager.persist(
                                campaign);

                entityManager.flush();

                return campaign;
        }

        private void persistExecution(
                        Campaign campaign,
                        LocalDateTime startedAt) {

                CampaignExecution execution = CampaignExecution.builder()
                                .campaign(
                                                campaign)
                                .status(
                                                CampaignExecutionStatus.COMPLETED)
                                .totalPatrols(2)
                                .completedPatrols(2)
                                .startedAt(
                                                startedAt)
                                .completedAt(
                                                startedAt.plusMinutes(
                                                                10))
                                .build();

                campaignExecutionRepository
                                .saveAndFlush(
                                                execution);
        }

}
