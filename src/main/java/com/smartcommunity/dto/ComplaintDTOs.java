package com.smartcommunity.dto;

import com.smartcommunity.enums.ComplaintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class ComplaintDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComplaintDTO {
        private Long id;
        private Long residentId;
        private String residentName;
        private Long flatId;
        private String flatNumber;
        private String buildingName;
        private Long assignedStaffId;
        private String assignedStaffName;
        private String title;
        private String category;
        private ComplaintStatus status;
        private String description;
        private String resolutionNotes;
        private Instant createdAt;
        private Instant resolvedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateComplaintRequest {
        @NotNull(message = "Resident ID is required")
        private Long residentId;

        @NotNull(message = "Flat ID is required")
        private Long flatId;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Category is required")
        private String category;

        @NotBlank(message = "Description is required")
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateComplaintStatusRequest {
        @NotNull(message = "Status is required")
        private ComplaintStatus status;

        private String resolutionNotes;

        private Long staffUserId;
    }
}
