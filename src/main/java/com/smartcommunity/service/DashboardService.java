package com.smartcommunity.service;

import com.smartcommunity.dto.DashboardDTOs.*;
import com.smartcommunity.enums.FlatStatus;
import com.smartcommunity.enums.PaymentStatus;
import com.smartcommunity.enums.RequestStatus;

import com.smartcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;
    private final VisitorRepository visitorRepository;
    private final MaintenanceRequestRepository maintenanceRepository;
    private final ComplaintRepository complaintRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public AdminDashboardDTO getAdminDashboardStats() {
        long totalResidents = residentRepository.count();
        long totalFlats = flatRepository.count();
        long occupiedFlats = flatRepository.countByStatus(FlatStatus.OCCUPIED);
        long vacantFlats = flatRepository.countByStatus(FlatStatus.VACANT);
        
        Instant startOfDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        long todayVisitors = visitorRepository.findExpectedVisitorsToday(startOfDay, endOfDay).size();

        long pendingMaint = maintenanceRepository.countByStatus(RequestStatus.PENDING);
        long pendingComp = complaintRepository.countByStatus(com.smartcommunity.enums.ComplaintStatus.PENDING);
        
        BigDecimal totalRev = paymentRepository.sumTotalCollected();
        if (totalRev == null) totalRev = BigDecimal.ZERO;

        return AdminDashboardDTO.builder()
                .totalResidents(totalResidents)
                .totalFlats(totalFlats)
                .occupiedFlats(occupiedFlats)
                .vacantFlats(vacantFlats)
                .todayVisitors(todayVisitors)
                .pendingMaintenance(pendingMaint)
                .pendingComplaints(pendingComp)
                .totalRevenueCollected(totalRev)
                .build();
    }

    @Transactional(readOnly = true)
    public SecurityDashboardDTO getSecurityDashboardStats() {
        Instant startOfDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

        long expected = visitorRepository.findExpectedVisitorsToday(startOfDay, endOfDay).size();
        long entries = visitorRepository.countEntriesToday(startOfDay, endOfDay);
        long exits = visitorRepository.countExitsToday(startOfDay, endOfDay);
        long pending = visitorRepository.findByStatus(com.smartcommunity.enums.VisitorStatus.PRE_REGISTERED).size();

        return SecurityDashboardDTO.builder()
                .expectedVisitorsToday(expected)
                .entriesToday(entries)
                .exitsToday(exits)
                .pendingVerifications(pending)
                .build();
    }

    @Transactional(readOnly = true)
    public StaffDashboardDTO getStaffDashboardStats(Long staffUserId) {
        long assigned = maintenanceRepository.countByAssignedStaffIdAndStatus(staffUserId, RequestStatus.ASSIGNED);
        long pending = maintenanceRepository.countByStatus(RequestStatus.PENDING);
        long inProgress = maintenanceRepository.countByAssignedStaffIdAndStatus(staffUserId, RequestStatus.IN_PROGRESS);
        long completed = maintenanceRepository.countByAssignedStaffIdAndStatus(staffUserId, RequestStatus.COMPLETED);

        return StaffDashboardDTO.builder()
                .assignedTasks(assigned)
                .pendingTasks(pending)
                .inProgressTasks(inProgress)
                .completedTasks(completed)
                .build();
    }
}
