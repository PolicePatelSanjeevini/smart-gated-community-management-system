package com.smartcommunity.dto;

import com.smartcommunity.enums.ResidentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ResidentDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResidentDTO {
        private Long id;
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private Long flatId;
        private String flatNumber;
        private String buildingName;
        private ResidentType residentType;
        private boolean isPrimary;
        private LocalDate moveInDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResidentRequest {
        @NotNull(message = "User ID is required")
        private Long userId;

        @NotNull(message = "Flat ID is required")
        private Long flatId;

        @NotNull(message = "Resident type is required")
        private ResidentType residentType;

        private boolean isPrimary;
        private LocalDate moveInDate;
    }
}
