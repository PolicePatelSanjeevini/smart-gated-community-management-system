package com.smartcommunity.dto;

import com.smartcommunity.enums.Priority;
import com.smartcommunity.enums.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class MaintenanceDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaintenanceRequestDTO {
        private Long id;
        private Long residentId;
        private String residentName;
        private Long flatId;
        private String flatNumber;
        private String buildingName;
        private Long assignedStaffId;
        private String assignedStaffName;
        private String category;
        private Priority priority;
        private RequestStatus status;
        private String description;
        private String imageUrl;
        private String completionNotes;
        private Instant createdAt;
        private Instant resolvedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMaintenanceRequest {
        @NotNull(message = "Resident ID is required")
        private Long residentId;

        @NotNull(message = "Flat ID is required")
        private Long flatId;

        @NotBlank(message = "Category is required")
        private String category;

        private Priority priority;

        @NotBlank(message = "Description is required")
        private String description;

        private String imageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignStaffRequest {
        @NotNull(message = "Staff User ID is required")
        private Long staffUserId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusRequest {
        @NotNull(message = "Status is required")
        private RequestStatus status;

        private String completionNotes;
    }
}
