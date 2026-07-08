package com.jastigi.silentcampaignmanager.service.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.AircraftRiskStrategy;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.ConfidenceModifier;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.SurfaceShipRiskStrategy;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.SubmarineRiskStrategy;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.UnknownRiskStrategy;

class ContactRiskCalculatorTest {

    private ContactRiskCalculator calculator;

    @BeforeEach
    void setUp() {
        ConfidenceModifier confidenceModifier = new ConfidenceModifier();
        calculator = new ContactRiskCalculator(List.of(
                new SubmarineRiskStrategy(confidenceModifier),
                new SurfaceShipRiskStrategy(confidenceModifier),
                new AircraftRiskStrategy(confidenceModifier),
                new UnknownRiskStrategy()));
    }

    @Test
    void ssbnHostileReturns40() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setNation(Nation.USSR);
        contact.setSubmarineClass(SubmarineClass.DELTA_IV);
        contact.setConfidenceLevel(100);

        assertEquals(40, calculator.calculate(List.of(contact)));
    }

    @Test
    void ssnHostileReturns30() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setNation(Nation.USSR);
        contact.setSubmarineClass(SubmarineClass.AKULA);
        contact.setConfidenceLevel(100);

        assertEquals(30, calculator.calculate(List.of(contact)));
    }

    @Test
    void friendlyContactReturns0() {

        Contact contact = new Contact();
        contact.setNation(Nation.USA);

        assertEquals(0, calculator.calculate(List.of(contact)));
    }

    @Test
    void unknownContactTypeReturns15() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.UNKNOWN);
        contact.setNation(Nation.USSR);

        assertEquals(15, calculator.calculate(List.of(contact)));
    }

    @Test
    void ssbnWithFullConfidenceReturns40() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setNation(Nation.USSR);
        contact.setSubmarineClass(SubmarineClass.DELTA_IV);
        contact.setConfidenceLevel(100);

        assertEquals(40, calculator.calculate(List.of(contact)));
    }

    @Test
    void ssbnWith60ConfidenceReturns30() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setNation(Nation.USSR);
        contact.setSubmarineClass(SubmarineClass.DELTA_IV);
        contact.setConfidenceLevel(60);

        assertEquals(30, calculator.calculate(List.of(contact)));
    }

    @Test
    void ssnWith80ConfidenceReturns27() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setNation(Nation.USSR);
        contact.setSubmarineClass(SubmarineClass.AKULA);
        contact.setConfidenceLevel(80);

        assertEquals(27, calculator.calculate(List.of(contact)));
    }

    @Test
    void surfaceHighWith50ConfidenceReturns15() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.SURFACE_SHIP);
        contact.setNation(Nation.USSR);
        contact.setThreatLevel(ThreatLevel.HIGH);
        contact.setConfidenceLevel(50);

        assertEquals(15, calculator.calculate(List.of(contact)));
    }

    @Test
    void aircraftCriticalWith30ConfidenceReturns20() {

        Contact contact = new Contact();
        contact.setContactType(ContactType.AIRCRAFT);
        contact.setNation(Nation.USSR);
        contact.setThreatLevel(ThreatLevel.CRITICAL);
        contact.setConfidenceLevel(30);

        assertEquals(20, calculator.calculate(List.of(contact)));
    }

}
