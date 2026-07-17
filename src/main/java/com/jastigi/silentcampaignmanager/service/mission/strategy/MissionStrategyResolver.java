package com.jastigi.silentcampaignmanager.service.mission.strategy;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;

@Component
public class MissionStrategyResolver {

        private final Map<MissionType, MissionStrategy> strategies = new EnumMap<>(MissionType.class);

        public MissionStrategyResolver(
                        HuntSsnMissionStrategy huntSsnMissionStrategy,
                        SurveillanceMissionstrategy surveillanceMissionStrategy,
                        IntelligenceMissionStrategy intelligenceMissionStrategy,
                        SpecialOperationMissionStrategy specialOperationMissionStrategy) {

                strategies.put(
                                MissionType.HUNT_SSN,
                                huntSsnMissionStrategy);

                strategies.put(
                                MissionType.SURVEILLANCE,
                                surveillanceMissionStrategy);

                strategies.put(
                                MissionType.INTELLIGENCE,
                                intelligenceMissionStrategy);

                strategies.put(
                                MissionType.SPECIAL_OPERATION,
                                specialOperationMissionStrategy);

        }

        public MissionStrategy resolve(MissionType missionType) {

                MissionStrategy strategy = strategies.get(missionType);

                if (strategy == null) {
                        return strategies.get(MissionType.SURVEILLANCE);
                }

                return strategy;

        }

}
