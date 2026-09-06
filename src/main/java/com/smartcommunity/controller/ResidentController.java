package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.ResidentDTOs.*;
import com.smartcommunity.service.ResidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
@RequiredArgsConstructor
@Tag(name = "Resident Management", description = "APIs for resident profiles and flat associations")
public class ResidentController {

    private final ResidentService residentService;

    @GetMapping
    @Operation(summary = "Get all residents directory")
    public ResponseEntity<ApiResponse<List<ResidentDTO>>> getAllResidents() {
        return ResponseEntity.ok(ApiResponse.success("Residents retrieved successfully", residentService.getAllResidents()));
    }

    @GetMapping("/flat/{flatId}")
    @Operation(summary = "Get residents by flat ID")
    public ResponseEntity<ApiResponse<List<ResidentDTO>>> getResidentsByFlat(@PathVariable Long flatId) {
        return ResponseEntity.ok(ApiResponse.success("Residents retrieved for flat", residentService.getResidentsByFlat(flatId)));
    }

    @PostMapping
    @Operation(summary = "Assign a resident to a flat")
    public ResponseEntity<ApiResponse<ResidentDTO>> createResident(@Valid @RequestBody CreateResidentRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Resident added successfully", residentService.createResident(request)),
                HttpStatus.CREATED
        );
    }
}
