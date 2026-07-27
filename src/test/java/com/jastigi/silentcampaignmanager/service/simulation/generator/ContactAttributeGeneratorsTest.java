package com.jastigi.silentcampaignmanager.service.simulation.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.generator.impl.ConfidenceLevelGeneratorImpl;
import com.jastigi.silentcampaignmanager.service.simulation.generator.impl.NationGeneratorImpl;
import com.jastigi.silentcampaignmanager.service.simulation.generator.impl.ThreatLevelGeneratorImpl;

@ExtendWith(MockitoExtension.class)
class ContactAttributeGeneratorsTest {

    @Mock
    private SimulationRandomService randomService;

    @Test
    void shouldGenerateNationUsingRandomService() {

        NationGenerator generator = new NationGeneratorImpl(
                randomService);

        when(randomService.pick(
                any(Nation[].class)))
                .thenReturn(Nation.USSR);

        Nation result = generator.generate(
                new Patrol());

        assertEquals(
                Nation.USSR,
                result);

        verify(randomService)
                .pick(any(Nation[].class));
    }

    @Test
    void shouldGenerateHighThreatForSubmarineRoll() {

        ThreatLevelGenerator generator = new ThreatLevelGeneratorImpl(
                randomService);

        when(randomService.range(1, 100))
                .thenReturn(70);

        assertEquals(
                ThreatLevel.HIGH,
                generator.generate(
                        ContactType.SUBMARINE));
    }

    @Test
    void shouldGenerateCriticalThreatForAircraftRoll() {

        ThreatLevelGenerator generator = new ThreatLevelGeneratorImpl(
                randomService);

        when(randomService.range(1, 100))
                .thenReturn(90);

        assertEquals(
                ThreatLevel.CRITICAL,
                generator.generate(
                        ContactType.AIRCRAFT));
    }

    @Test
    void shouldReturnLowThreatForNullContactType() {

        ThreatLevelGenerator generator = new ThreatLevelGeneratorImpl(
                randomService);

        assertEquals(
                ThreatLevel.LOW,
                generator.generate(null));
    }

    @Test
    void shouldUseSubmarineConfidenceRange() {

        ConfidenceLevelGenerator generator = new ConfidenceLevelGeneratorImpl(
                randomService);

        when(randomService.range(45, 80))
                .thenReturn(68);

        assertEquals(
                68,
                generator.generate(
                        ContactType.SUBMARINE));

        verify(randomService)
                .range(45, 80);
    }

    @Test
    void shouldUseSurfaceShipConfidenceRange() {

        ConfidenceLevelGenerator generator = new ConfidenceLevelGeneratorImpl(
                randomService);

        when(randomService.range(65, 95))
                .thenReturn(90);

        assertEquals(
                90,
                generator.generate(
                        ContactType.SURFACE_SHIP));

        verify(randomService)
                .range(65, 95);
    }

    @Test
    void shouldUseUnknownConfidenceRangeForNullType() {

        ConfidenceLevelGenerator generator = new ConfidenceLevelGeneratorImpl(
                randomService);

        when(randomService.range(20, 40))
                .thenReturn(30);

        assertEquals(
                30,
                generator.generate(null));

        verify(randomService)
                .range(20, 40);
    }

}
