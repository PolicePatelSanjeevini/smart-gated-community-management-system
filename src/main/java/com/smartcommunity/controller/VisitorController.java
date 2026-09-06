package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.VisitorDTOs.*;
import com.smartcommunity.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
@Tag(name = "Visitor Management", description = "APIs for visitor pre-registration, security check-in, and exit tracking")
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping
    @Operation(summary = "Get visitor history & today activity")
    public ResponseEntity<ApiResponse<List<VisitorDTO>>> getAllVisitors() {
        return ResponseEntity.ok(ApiResponse.success("Visitors retrieved successfully", visitorService.getAllVisitors()));
    }

    @PostMapping("/pre-register")
    @Operation(summary = "Pre-register an expected visitor (Resident/Admin)")
    public ResponseEntity<ApiResponse<VisitorDTO>> registerVisitor(@Valid @RequestBody RegisterVisitorRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Visitor registered successfully", visitorService.registerVisitor(request)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/check-in")
    @Operation(summary = "Record visitor entry at security gate (Security Guard)")
    public ResponseEntity<ApiResponse<VisitorDTO>> recordEntry(@Valid @RequestBody EntryExitActionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Visitor entry recorded successfully", visitorService.recordEntry(request)));
    }

    @PostMapping("/check-out")
    @Operation(summary = "Record visitor exit at security gate (Security Guard)")
    public ResponseEntity<ApiResponse<VisitorDTO>> recordExit(@Valid @RequestBody EntryExitActionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Visitor exit recorded successfully", visitorService.recordExit(request)));
    }
}
