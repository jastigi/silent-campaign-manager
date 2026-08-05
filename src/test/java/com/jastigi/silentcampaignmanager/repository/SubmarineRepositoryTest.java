package com.jastigi.silentcampaignmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;

@DataJpaTest
class SubmarineRepositoryTest {

    @Autowired
    private SubmarineRepository submarineRepository;

    @Test
    void shouldFindSubmarinesByStatus() {

        Submarine activeSubmarine = submarine(
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        Submarine refitSubmarine = submarine(
                "USS Michigan",
                SubmarineStatus.REFIT);

        Submarine damagedSubmarine = submarine(
                "USS Florida",
                SubmarineStatus.DAMAGED);

        submarineRepository.saveAllAndFlush(
                List.of(
                        activeSubmarine,
                        refitSubmarine,
                        damagedSubmarine));

        List<Submarine> result = submarineRepository.findByStatus(
                SubmarineStatus.ACTIVE);

        assertEquals(
                1,
                result.size());

        assertEquals(
                "USS Ohio",
                result.getFirst()
                        .getName());

        assertEquals(
                SubmarineStatus.ACTIVE,
                result.getFirst()
                        .getStatus());
    }

    @Test
    void shouldReturnEmptyListWhenStatusHasNoSubmarines() {

        submarineRepository.saveAndFlush(
                submarine(
                        "USS Ohio",
                        SubmarineStatus.ACTIVE));

        List<Submarine> result = submarineRepository.findByStatus(
                SubmarineStatus.RETIRED);

        assertTrue(
                result.isEmpty());
    }

    private Submarine submarine(
            String name,
            SubmarineStatus status) {

        Submarine submarine = new Submarine();

        submarine.setName(
                name);

        submarine.setSubmarineType(
                SubmarineType.SSBN);

        submarine.setSubmarineClass(
                SubmarineClass.OHIO);

        submarine.setNation(
                "USA");

        submarine.setStatus(
                status);

        return submarine;
    }

}
