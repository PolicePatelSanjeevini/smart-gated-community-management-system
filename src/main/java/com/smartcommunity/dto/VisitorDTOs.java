package com.smartcommunity.dto;

import com.smartcommunity.enums.VisitorStatus;
import com.smartcommunity.enums.VisitorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class VisitorDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitorDTO {
        private Long id;
        private Long residentId;
        private String residentName;
        private String residentPhone;
        private Long flatId;
        private String flatNumber;
        private String buildingName;
        private String name;
        private String phoneNumber;
        private String vehicleNumber;
        private String purpose;
        private VisitorType visitorType;
        private Instant expectedArrival;
        private Instant entryTime;
        private Instant exitTime;
        private VisitorStatus status;
        private String accessCode;
        private String verifiedByGuardName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterVisitorRequest {
        @NotNull(message = "Resident ID is required")
        private Long residentId;

        @NotNull(message = "Flat ID is required")
        private Long flatId;

        @NotBlank(message = "Visitor name is required")
        private String name;

        @NotBlank(message = "Phone number is required")
        private String phoneNumber;

        private String vehicleNumber;

        @NotBlank(message = "Purpose of visit is required")
        private String purpose;

        @NotNull(message = "Visitor type is required")
        private VisitorType visitorType;

        @NotNull(message = "Expected arrival time is required")
        private Instant expectedArrival;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntryExitActionRequest {
        @NotBlank(message = "Access code is required")
        private String accessCode;

        private Long guardUserId;
    }
}
