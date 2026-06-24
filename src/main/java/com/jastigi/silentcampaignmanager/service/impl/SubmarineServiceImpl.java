package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.exception.SubmarineNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.SubmarineMapper;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;
import com.jastigi.silentcampaignmanager.service.SubmarineService;

@Service
public class SubmarineServiceImpl implements SubmarineService {

    private final SubmarineRepository submarineRepository;

    public SubmarineServiceImpl(
            SubmarineRepository submarineRepository) {

        this.submarineRepository = submarineRepository;
    }

    @Override
    public SubmarineResponseDTO createSubmarine(
            SubmarineRequestDTO request) {

        Submarine submarine = SubmarineMapper.toEntity(request);

        Submarine saved = submarineRepository.save(submarine);

        return SubmarineMapper.toDTO(saved);
    }

    @Override
    public List<SubmarineResponseDTO> getAllSubmarines() {

        return submarineRepository.findAll()
                .stream()
                .map(SubmarineMapper::toDTO)
                .toList();
    }

    @Override
    public SubmarineResponseDTO getSubmarineById(
            Long id) {

        Submarine submarine = submarineRepository.findById(id)
                .orElseThrow(() -> new SubmarineNotFoundException(id));

        return SubmarineMapper.toDTO(submarine);
    }

    @Override
    public SubmarineResponseDTO updateSubmarine(
            Long id,
            SubmarineRequestDTO request) {

        Submarine submarine = submarineRepository.findById(id)
                .orElseThrow(() -> new SubmarineNotFoundException(id));

        submarine.setName(request.getName());
        submarine.setSubmarineType(request.getType());
        submarine.setSubmarineClass(request.getSubmarineClass());
        submarine.setNation(request.getNation());
        submarine.setStatus(request.getStatus());

        Submarine updated = submarineRepository.save(submarine);

        return SubmarineMapper.toDTO(updated);
    }

    @Override
    public void deleteSubmarine(
            Long id) {

        Submarine submarine = submarineRepository.findById(id)
                .orElseThrow(() -> new SubmarineNotFoundException(id));

        submarineRepository.delete(submarine);
    }

}
