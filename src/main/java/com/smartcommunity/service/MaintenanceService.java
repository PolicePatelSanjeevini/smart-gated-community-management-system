package com.smartcommunity.service;

import com.smartcommunity.dto.MaintenanceDTOs.*;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.entity.MaintenanceRequest;
import com.smartcommunity.entity.Resident;
import com.smartcommunity.entity.User;
import com.smartcommunity.enums.Priority;
import com.smartcommunity.enums.RequestStatus;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.FlatRepository;
import com.smartcommunity.repository.MaintenanceRequestRepository;
import com.smartcommunity.repository.ResidentRepository;
import com.smartcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRequestRepository maintenanceRepository;
    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MaintenanceRequestDTO> getAllRequests() {
        return maintenanceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceRequestDTO createRequest(CreateMaintenanceRequest request) {
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", request.getResidentId()));

        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", request.getFlatId()));

        MaintenanceRequest req = MaintenanceRequest.builder()
                .resident(resident)
                .flat(flat)
                .category(request.getCategory())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .status(RequestStatus.PENDING)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        MaintenanceRequest saved = maintenanceRepository.save(req);
        return mapToDTO(saved);
    }

    @Transactional
    public MaintenanceRequestDTO assignStaff(Long requestId, Long staffUserId) {
        MaintenanceRequest req = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceRequest", "id", requestId));

        User staff = userRepository.findById(staffUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User (Staff)", "id", staffUserId));

        req.setAssignedStaff(staff);
        req.setStatus(RequestStatus.ASSIGNED);

        MaintenanceRequest saved = maintenanceRepository.save(req);
        return mapToDTO(saved);
    }

    @Transactional
    public MaintenanceRequestDTO updateStatus(Long requestId, UpdateStatusRequest request) {
        MaintenanceRequest req = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceRequest", "id", requestId));

        req.setStatus(request.getStatus());
        if (request.getCompletionNotes() != null) {
            req.setCompletionNotes(request.getCompletionNotes());
        }

        if (request.getStatus() == RequestStatus.COMPLETED) {
            req.setResolvedAt(Instant.now());
        }

        MaintenanceRequest saved = maintenanceRepository.save(req);
        return mapToDTO(saved);
    }

    public MaintenanceRequestDTO mapToDTO(MaintenanceRequest m) {
        return MaintenanceRequestDTO.builder()
                .id(m.getId())
                .residentId(m.getResident().getId())
                .residentName(m.getResident().getUser().getFirstName() + " " + m.getResident().getUser().getLastName())
                .flatId(m.getFlat().getId())
                .flatNumber(m.getFlat().getFlatNumber())
                .buildingName(m.getFlat().getBuilding().getName())
                .assignedStaffId(m.getAssignedStaff() != null ? m.getAssignedStaff().getId() : null)
                .assignedStaffName(m.getAssignedStaff() != null ? m.getAssignedStaff().getFirstName() + " " + m.getAssignedStaff().getLastName() : "Unassigned")
                .category(m.getCategory())
                .priority(m.getPriority())
                .status(m.getStatus())
                .description(m.getDescription())
                .imageUrl(m.getImageUrl())
                .completionNotes(m.getCompletionNotes())
                .createdAt(m.getCreatedAt())
                .resolvedAt(m.getResolvedAt())
                .build();
    }
}
