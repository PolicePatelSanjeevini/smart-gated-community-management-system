package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.MaintenanceDTOs.*;
import com.smartcommunity.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@Tag(name = "Maintenance Management", description = "APIs for resident maintenance requests and staff work orders")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "Get all maintenance requests")
    public ResponseEntity<ApiResponse<List<MaintenanceRequestDTO>>> getAllRequests() {
        return ResponseEntity.ok(ApiResponse.success("Maintenance requests retrieved", maintenanceService.getAllRequests()));
    }

    @PostMapping
    @Operation(summary = "Create a maintenance request (Resident/Admin)")
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> createRequest(@Valid @RequestBody CreateMaintenanceRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Maintenance request created successfully", maintenanceService.createRequest(request)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign maintenance staff to request (Admin)")
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> assignStaff(
            @PathVariable Long id,
            @Valid @RequestBody AssignStaffRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Staff assigned successfully", maintenanceService.assignStaff(id, request.getStaffUserId())));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update request status & completion notes (Staff/Admin)")
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Request status updated successfully", maintenanceService.updateStatus(id, request)));
    }
}
