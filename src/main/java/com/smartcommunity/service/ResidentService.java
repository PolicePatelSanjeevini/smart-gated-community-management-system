package com.smartcommunity.service;

import com.smartcommunity.dto.ResidentDTOs.*;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.entity.Resident;
import com.smartcommunity.entity.User;
import com.smartcommunity.enums.FlatStatus;
import com.smartcommunity.exception.DuplicateResourceException;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.FlatRepository;
import com.smartcommunity.repository.ResidentRepository;
import com.smartcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final UserRepository userRepository;
    private final FlatRepository flatRepository;

    @Transactional(readOnly = true)
    public List<ResidentDTO> getAllResidents() {
        return residentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResidentDTO> getResidentsByFlat(Long flatId) {
        return residentRepository.findByFlatId(flatId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentDTO createResident(CreateResidentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", request.getFlatId()));

        if (residentRepository.existsByUserIdAndFlatId(request.getUserId(), request.getFlatId())) {
            throw new DuplicateResourceException("Resident relationship already exists for user and flat");
        }

        Resident resident = Resident.builder()
                .user(user)
                .flat(flat)
                .residentType(request.getResidentType())
                .isPrimary(request.isPrimary())
                .moveInDate(request.getMoveInDate())
                .build();

        Resident saved = residentRepository.save(resident);

        // Update flat status to OCCUPIED
        flat.setStatus(FlatStatus.OCCUPIED);
        flatRepository.save(flat);

        return mapToDTO(saved);
    }

    public ResidentDTO mapToDTO(Resident r) {
        return ResidentDTO.builder()
                .id(r.getId())
                .userId(r.getUser().getId())
                .firstName(r.getUser().getFirstName())
                .lastName(r.getUser().getLastName())
                .email(r.getUser().getEmail())
                .phoneNumber(r.getUser().getPhoneNumber())
                .flatId(r.getFlat().getId())
                .flatNumber(r.getFlat().getFlatNumber())
                .buildingName(r.getFlat().getBuilding().getName())
                .residentType(r.getResidentType())
                .isPrimary(r.isPrimary())
                .moveInDate(r.getMoveInDate())
                .build();
    }
}
