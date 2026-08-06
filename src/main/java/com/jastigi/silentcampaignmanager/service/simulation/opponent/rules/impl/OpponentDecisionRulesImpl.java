package com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecision;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.model.OpponentDecisionType;
import com.jastigi.silentcampaignmanager.service.simulation.opponent.rules.OpponentDecisionRules;

@Component
public class OpponentDecisionRulesImpl
        implements OpponentDecisionRules {

    private static final int HIGH_CONFIDENCE =
            75;

    private static final int MEDIUM_CONFIDENCE =
            50;

    @Override
    public OpponentDecision evaluate(
            DetectedContact contact) {

        validateContact(
                contact);

        ThreatLevel threatLevel =
                contact.getThreatLevel();

        int confidenceLevel =
                normalizeConfidence(
                        contact.getConfidenceLevel());

        ContactType contactType =
                contact.getContactType();

        if (threatLevel
                == ThreatLevel.CRITICAL) {

            return decision(
                    OpponentDecisionType.INTERCEPT,
                    "Critical threat requires immediate interception");
        }

        if (threatLevel
                == ThreatLevel.HIGH) {

            if (confidenceLevel
                    >= HIGH_CONFIDENCE) {

                return decision(
                        OpponentDecisionType.INTERCEPT,
                        "High-threat contact identified with high confidence");
            }

            return decision(
                    OpponentDecisionType.INVESTIGATE,
                    "High-threat contact requires further identification");
        }

        if (threatLevel
                == ThreatLevel.MEDIUM) {

            if (contactType
                    == ContactType.SUBMARINE
                    && confidenceLevel
                            >= HIGH_CONFIDENCE) {

                return decision(
                        OpponentDecisionType.INTERCEPT,
                        "Confirmed submarine contact requires interception");
            }

            if (confidenceLevel
                    >= MEDIUM_CONFIDENCE) {

                return decision(
                        OpponentDecisionType.INVESTIGATE,
                        "Medium-threat contact requires investigation");
            }

            return decision(
                    OpponentDecisionType.MONITOR,
                    "Medium-threat contact remains under observation");
        }

        if (confidenceLevel
                >= HIGH_CONFIDENCE
                && contactType
                        == ContactType.SUBMARINE) {

            return decision(
                    OpponentDecisionType.MONITOR,
                    "Confirmed low-threat submarine contact remains under observation");
        }

        return decision(
                OpponentDecisionType.IGNORE,
                "Low-threat contact does not justify an active response");
    }

    private void validateContact(
            DetectedContact contact) {

        if (contact == null) {

            throw new IllegalArgumentException(
                    "Detected contact must not be null");
        }

        if (contact.getThreatLevel() == null) {

            throw new IllegalArgumentException(
                    "Detected contact threat level must not be null");
        }

        if (contact.getContactType() == null) {

            throw new IllegalArgumentException(
                    "Detected contact type must not be null");
        }
    }

    private int normalizeConfidence(
            int confidenceLevel) {

        return Math.max(
                0,
                Math.min(
                        100,
                        confidenceLevel));
    }

    private OpponentDecision decision(
            OpponentDecisionType type,
            String rationale) {

        return new OpponentDecision(
                type,
                rationale);
    }

}
