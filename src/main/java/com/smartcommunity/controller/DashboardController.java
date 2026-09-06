package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.DashboardDTOs.*;
import com.smartcommunity.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Role Dashboards & Analytics", description = "APIs for role-specific aggregate metrics and real-time community stats")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @Operation(summary = "Get Admin dashboard metrics")
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> getAdminDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard metrics", dashboardService.getAdminDashboardStats()));
    }

    @GetMapping("/guard")
    @Operation(summary = "Get Security Guard gate activity metrics")
    public ResponseEntity<ApiResponse<SecurityDashboardDTO>> getSecurityDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Security dashboard metrics", dashboardService.getSecurityDashboardStats()));
    }

    @GetMapping("/staff/{staffUserId}")
    @Operation(summary = "Get Maintenance Staff task metrics")
    public ResponseEntity<ApiResponse<StaffDashboardDTO>> getStaffDashboard(@PathVariable Long staffUserId) {
        return ResponseEntity.ok(ApiResponse.success("Staff dashboard metrics", dashboardService.getStaffDashboardStats(staffUserId)));
    }
}
