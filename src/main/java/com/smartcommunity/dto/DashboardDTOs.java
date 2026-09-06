package com.smartcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class DashboardDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminDashboardDTO {
        private long totalResidents;
        private long totalFlats;
        private long occupiedFlats;
        private long vacantFlats;
        private long todayVisitors;
        private long pendingMaintenance;
        private long pendingComplaints;
        private BigDecimal totalRevenueCollected;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResidentDashboardDTO {
        private long upcomingVisitorsCount;
        private long activeMaintenanceCount;
        private long activeComplaintsCount;
        private long pendingPaymentsCount;
        private BigDecimal totalPendingDues;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityDashboardDTO {
        private long expectedVisitorsToday;
        private long entriesToday;
        private long exitsToday;
        private long pendingVerifications;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffDashboardDTO {
        private long assignedTasks;
        private long pendingTasks;
        private long inProgressTasks;
        private long completedTasks;
    }
}
