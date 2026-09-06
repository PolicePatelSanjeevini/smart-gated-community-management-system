package com.smartcommunity.dto;

import com.smartcommunity.enums.FlatStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BuildingFlatDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildingDTO {
        private Long id;
        private String name;
        private Integer totalFloors;
        private String description;
        private Integer totalFlats;
        private Integer occupiedFlats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBuildingRequest {
        @NotBlank(message = "Building name is required")
        private String name;

        @NotNull(message = "Total floors is required")
        private Integer totalFloors;

        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlatDTO {
        private Long id;
        private Long buildingId;
        private String buildingName;
        private String flatNumber;
        private Integer floorNumber;
        private FlatStatus status;
        private Integer residentCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFlatRequest {
        @NotNull(message = "Building ID is required")
        private Long buildingId;

        @NotBlank(message = "Flat number is required")
        private String flatNumber;

        @NotNull(message = "Floor number is required")
        private Integer floorNumber;

        private FlatStatus status;
    }
}
