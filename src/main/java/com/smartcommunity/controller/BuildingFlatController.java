package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.BuildingFlatDTOs.*;
import com.smartcommunity.service.BuildingFlatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Building & Flat Management", description = "APIs for managing residential community buildings and flat units")
public class BuildingFlatController {

    private final BuildingFlatService buildingFlatService;

    @GetMapping("/buildings")
    @Operation(summary = "Get all buildings")
    public ResponseEntity<ApiResponse<List<BuildingDTO>>> getAllBuildings() {
        return ResponseEntity.ok(ApiResponse.success("Buildings retrieved successfully", buildingFlatService.getAllBuildings()));
    }

    @PostMapping("/admin/buildings")
    @Operation(summary = "Create a new building (Admin)")
    public ResponseEntity<ApiResponse<BuildingDTO>> createBuilding(@Valid @RequestBody CreateBuildingRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Building created successfully", buildingFlatService.createBuilding(request)),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/flats")
    @Operation(summary = "Get all flats")
    public ResponseEntity<ApiResponse<List<FlatDTO>>> getAllFlats() {
        return ResponseEntity.ok(ApiResponse.success("Flats retrieved successfully", buildingFlatService.getAllFlats()));
    }

    @GetMapping("/buildings/{buildingId}/flats")
    @Operation(summary = "Get flats by building ID")
    public ResponseEntity<ApiResponse<List<FlatDTO>>> getFlatsByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(ApiResponse.success("Building flats retrieved successfully", buildingFlatService.getFlatsByBuilding(buildingId)));
    }

    @PostMapping("/admin/flats")
    @Operation(summary = "Create a new flat (Admin)")
    public ResponseEntity<ApiResponse<FlatDTO>> createFlat(@Valid @RequestBody CreateFlatRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Flat created successfully", buildingFlatService.createFlat(request)),
                HttpStatus.CREATED
        );
    }
}
