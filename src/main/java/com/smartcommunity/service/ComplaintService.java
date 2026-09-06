package com.smartcommunity.service;

import com.smartcommunity.dto.ComplaintDTOs.*;
import com.smartcommunity.entity.Complaint;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.entity.Resident;
import com.smartcommunity.entity.User;
import com.smartcommunity.enums.ComplaintStatus;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.ComplaintRepository;
import com.smartcommunity.repository.FlatRepository;
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
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ComplaintDTO> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintDTO createComplaint(CreateComplaintRequest request) {
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", request.getResidentId()));

        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", request.getFlatId()));

        Complaint complaint = Complaint.builder()
                .resident(resident)
                .flat(flat)
                .title(request.getTitle())
                .category(request.getCategory())
                .status(ComplaintStatus.PENDING)
                .description(request.getDescription())
                .build();

        Complaint saved = complaintRepository.save(complaint);
        return mapToDTO(saved);
    }

    @Transactional
    public ComplaintDTO updateStatus(Long complaintId, UpdateComplaintStatusRequest request) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", complaintId));

        complaint.setStatus(request.getStatus());
        if (request.getResolutionNotes() != null) {
            complaint.setResolutionNotes(request.getResolutionNotes());
        }

        if (request.getStaffUserId() != null) {
            User staff = userRepository.findById(request.getStaffUserId()).orElse(null);
            complaint.setAssignedStaff(staff);
        }

        if (request.getStatus() == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(Instant.now());
        }

        Complaint saved = complaintRepository.save(complaint);
        return mapToDTO(saved);
    }

    public ComplaintDTO mapToDTO(Complaint c) {
        return ComplaintDTO.builder()
                .id(c.getId())
                .residentId(c.getResident().getId())
                .residentName(c.getResident().getUser().getFirstName() + " " + c.getResident().getUser().getLastName())
                .flatId(c.getFlat().getId())
                .flatNumber(c.getFlat().getFlatNumber())
                .buildingName(c.getFlat().getBuilding().getName())
                .assignedStaffId(c.getAssignedStaff() != null ? c.getAssignedStaff().getId() : null)
                .assignedStaffName(c.getAssignedStaff() != null ? c.getAssignedStaff().getFirstName() + " " + c.getAssignedStaff().getLastName() : "Unassigned")
                .title(c.getTitle())
                .category(c.getCategory())
                .status(c.getStatus())
                .description(c.getDescription())
                .resolutionNotes(c.getResolutionNotes())
                .createdAt(c.getCreatedAt())
                .resolvedAt(c.getResolvedAt())
                .build();
    }
}
