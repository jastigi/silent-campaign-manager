package com.jastigi.silentcampaignmanager.service.missions.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.NationAlignment;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.missions.MissionValues;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

@Component
public class IntelligenceMissionEvaluator implements MissionEvaluator {

    @Override
    public MissionType getSupportedMission() {

        return MissionType.INTELLIGENCE;

    }

    @Override
    public MissionEvaluationResult evaluate(Patrol patrol) {

        int score = calculateScore(patrol);

        boolean success = score >= MissionValues.SUCCESS_SCORE;

        return MissionEvaluationResult.builder()
                .score(score)
                .success(success)
                .summary(buildSummary(score, success))
                .build();

    }

    private int calculateScore(Patrol patrol) {

        int score = 0;

        for (Contact contact : patrol.getContacts()) {

            score += calculateContactScore(contact);

        }

        return score;

    }

    private int calculateContactScore(Contact contact) {

        int score = 0;

        if (contact.getNation().getAlignment() == NationAlignment.HOSTILE) {

            score += MissionValues.HOSTILE_CONTACT;

        }

        if (contact.getConfidenceLevel() != null
                && contact.getConfidenceLevel() >= 75) {

            score += MissionValues.HIGH_CONFIDENCE;

        }

        if (contact.getThreatLevel() == ThreatLevel.HIGH
                || contact.getThreatLevel() == ThreatLevel.CRITICAL) {

            score += MissionValues.HIGH_THREAT;

        }

        return score;

    }

    private String buildSummary(int score, boolean success) {

        if (success) {

            return "Mission accomplished. Intelligence objectives achieved.";

        }

        return "Mission failed. Insufficient intelligence gathered.";

    }

}
