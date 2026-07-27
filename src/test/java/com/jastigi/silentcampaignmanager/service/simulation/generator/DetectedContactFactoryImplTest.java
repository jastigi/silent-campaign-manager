package com.jastigi.silentcampaignmanager.service.simulation.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.generator.impl.DetectedContactFactoryImpl;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

@ExtendWith(MockitoExtension.class)
class DetectedContactFactoryImplTest {

    @Mock
    private ContactTypeGenerator contactTypeGenerator;

    @Mock
    private NationGenerator nationGenerator;

    @Mock
    private ThreatLevelGenerator threatLevelGenerator;

    @Mock
    private ConfidenceLevelGenerator confidenceLevelGenerator;

    private DetectedContactFactory factory;

    @BeforeEach
    void setUp() {

        factory = new DetectedContactFactoryImpl(
                contactTypeGenerator,
                nationGenerator,
                threatLevelGenerator,
                confidenceLevelGenerator);
    }

    @Test
    void shouldCreateDetectedContactUsingAllGenerators() {

        Patrol patrol = new Patrol();

        when(contactTypeGenerator.generate(patrol))
                .thenReturn(
                        ContactType.SUBMARINE);

        when(nationGenerator.generate(patrol))
                .thenReturn(Nation.USSR);

        when(threatLevelGenerator.generate(
                ContactType.SUBMARINE))
                .thenReturn(
                        ThreatLevel.HIGH);

        when(confidenceLevelGenerator.generate(
                ContactType.SUBMARINE))
                .thenReturn(78);

        DetectedContact contact = factory.create(patrol);

        assertEquals(
                ContactType.SUBMARINE,
                contact.getContactType());

        assertEquals(
                Nation.USSR,
                contact.getNation());

        assertEquals(
                ThreatLevel.HIGH,
                contact.getThreatLevel());

        assertEquals(
                78,
                contact.getConfidenceLevel());

        verify(contactTypeGenerator)
                .generate(patrol);

        verify(nationGenerator)
                .generate(patrol);

        verify(threatLevelGenerator)
                .generate(
                        ContactType.SUBMARINE);

        verify(confidenceLevelGenerator)
                .generate(
                        ContactType.SUBMARINE);
    }

}
