package com.smartcommunity.service;

import com.smartcommunity.dto.BuildingFlatDTOs.*;
import com.smartcommunity.entity.Building;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.enums.FlatStatus;
import com.smartcommunity.exception.DuplicateResourceException;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.BuildingRepository;
import com.smartcommunity.repository.FlatRepository;
import com.smartcommunity.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingFlatService {

    private final BuildingRepository buildingRepository;
    private final FlatRepository flatRepository;
    private final ResidentRepository residentRepository;

    @Transactional(readOnly = true)
    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(this::mapToBuildingDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BuildingDTO createBuilding(CreateBuildingRequest request) {
        if (buildingRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Building", "name", request.getName());
        }

        Building building = Building.builder()
                .name(request.getName())
                .totalFloors(request.getTotalFloors())
                .description(request.getDescription())
                .build();

        Building saved = buildingRepository.save(building);
        return mapToBuildingDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<FlatDTO> getAllFlats() {
        return flatRepository.findAll().stream()
                .map(this::mapToFlatDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FlatDTO> getFlatsByBuilding(Long buildingId) {
        return flatRepository.findByBuildingId(buildingId).stream()
                .map(this::mapToFlatDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FlatDTO createFlat(CreateFlatRequest request) {
        Building building = buildingRepository.findById(request.getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", request.getBuildingId()));

        if (flatRepository.existsByBuildingIdAndFlatNumber(request.getBuildingId(), request.getFlatNumber())) {
            throw new DuplicateResourceException("Flat", "flatNumber", request.getFlatNumber());
        }

        Flat flat = Flat.builder()
                .building(building)
                .flatNumber(request.getFlatNumber())
                .floorNumber(request.getFloorNumber())
                .status(request.getStatus() != null ? request.getStatus() : FlatStatus.VACANT)
                .build();

        Flat saved = flatRepository.save(flat);
        return mapToFlatDTO(saved);
    }

    private BuildingDTO mapToBuildingDTO(Building b) {
        List<Flat> flats = flatRepository.findByBuildingId(b.getId());
        long occupiedCount = flats.stream().filter(f -> f.getStatus() == FlatStatus.OCCUPIED).count();

        return BuildingDTO.builder()
                .id(b.getId())
                .name(b.getName())
                .totalFloors(b.getTotalFloors())
                .description(b.getDescription())
                .totalFlats(flats.size())
                .occupiedFlats((int) occupiedCount)
                .build();
    }

    private FlatDTO mapToFlatDTO(Flat f) {
        int residentCount = residentRepository.findByFlatId(f.getId()).size();
        return FlatDTO.builder()
                .id(f.getId())
                .buildingId(f.getBuilding().getId())
                .buildingName(f.getBuilding().getName())
                .flatNumber(f.getFlatNumber())
                .floorNumber(f.getFloorNumber())
                .status(f.getStatus())
                .residentCount(residentCount)
                .build();
    }
}
