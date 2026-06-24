package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;

public interface SubmarineService {

    SubmarineResponseDTO createSubmarine(SubmarineRequestDTO request);

    List<SubmarineResponseDTO> getAllSubmarines();

    SubmarineResponseDTO getSubmarineById(Long id);

    SubmarineResponseDTO updateSubmarine(Long id, SubmarineRequestDTO request);

    void deleteSubmarine(Long id);

}
