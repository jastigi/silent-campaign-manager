package com.jastigi.silentcampaignmanager.service.campaign.patrol.generator;

import java.util.List;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;

public interface PatrolGenerator {

    List<Patrol> generatePatrols(Campaign campaign, List<Submarine> submarines);

}
