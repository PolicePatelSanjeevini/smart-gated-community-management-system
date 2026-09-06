package com.smartcommunity.service;

import com.smartcommunity.dto.VisitorDTOs.*;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.entity.Resident;
import com.smartcommunity.entity.User;
import com.smartcommunity.entity.Visitor;
import com.smartcommunity.enums.VisitorStatus;
import com.smartcommunity.exception.BadRequestException;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.FlatRepository;
import com.smartcommunity.repository.ResidentRepository;
import com.smartcommunity.repository.UserRepository;
import com.smartcommunity.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<VisitorDTO> getAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitorDTO registerVisitor(RegisterVisitorRequest request) {
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", request.getResidentId()));

        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", request.getFlatId()));

        String accessCode = "VIS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Visitor visitor = Visitor.builder()
                .resident(resident)
                .flat(flat)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .vehicleNumber(request.getVehicleNumber())
                .purpose(request.getPurpose())
                .visitorType(request.getVisitorType())
                .expectedArrival(request.getExpectedArrival())
                .status(VisitorStatus.PRE_REGISTERED)
                .accessCode(accessCode)
                .build();

        Visitor saved = visitorRepository.save(visitor);
        return mapToDTO(saved);
    }

    @Transactional
    public VisitorDTO recordEntry(EntryExitActionRequest request) {
        Visitor visitor = visitorRepository.findByAccessCode(request.getAccessCode())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor", "accessCode", request.getAccessCode()));

        if (visitor.getStatus() == VisitorStatus.INSIDE) {
            throw new BadRequestException("Visitor has already checked in");
        }

        if (visitor.getStatus() == VisitorStatus.CHECKED_OUT) {
            throw new BadRequestException("Visitor has already checked out");
        }

        visitor.setStatus(VisitorStatus.INSIDE);
        visitor.setEntryTime(Instant.now());

        if (request.getGuardUserId() != null) {
            User guard = userRepository.findById(request.getGuardUserId()).orElse(null);
            visitor.setVerifiedByGuard(guard);
        }

        Visitor updated = visitorRepository.save(visitor);
        return mapToDTO(updated);
    }

    @Transactional
    public VisitorDTO recordExit(EntryExitActionRequest request) {
        Visitor visitor = visitorRepository.findByAccessCode(request.getAccessCode())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor", "accessCode", request.getAccessCode()));

        if (visitor.getStatus() != VisitorStatus.INSIDE) {
            throw new BadRequestException("Visitor must be inside the community to record exit");
        }

        visitor.setStatus(VisitorStatus.CHECKED_OUT);
        visitor.setExitTime(Instant.now());

        Visitor updated = visitorRepository.save(visitor);
        return mapToDTO(updated);
    }

    public VisitorDTO mapToDTO(Visitor v) {
        return VisitorDTO.builder()
                .id(v.getId())
                .residentId(v.getResident().getId())
                .residentName(v.getResident().getUser().getFirstName() + " " + v.getResident().getUser().getLastName())
                .residentPhone(v.getResident().getUser().getPhoneNumber())
                .flatId(v.getFlat().getId())
                .flatNumber(v.getFlat().getFlatNumber())
                .buildingName(v.getFlat().getBuilding().getName())
                .name(v.getName())
                .phoneNumber(v.getPhoneNumber())
                .vehicleNumber(v.getVehicleNumber())
                .purpose(v.getPurpose())
                .visitorType(v.getVisitorType())
                .expectedArrival(v.getExpectedArrival())
                .entryTime(v.getEntryTime())
                .exitTime(v.getExitTime())
                .status(v.getStatus())
                .accessCode(v.getAccessCode())
                .verifiedByGuardName(v.getVerifiedByGuard() != null ? v.getVerifiedByGuard().getFirstName() + " " + v.getVerifiedByGuard().getLastName() : null)
                .build();
    }
}
