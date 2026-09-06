package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.NoticeDTOs.*;
import com.smartcommunity.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice & Announcements Management", description = "APIs for publishing and viewing community announcements")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    @Operation(summary = "Get all published community notices")
    public ResponseEntity<ApiResponse<List<NoticeDTO>>> getAllNotices() {
        return ResponseEntity.ok(ApiResponse.success("Notices retrieved successfully", noticeService.getAllNotices()));
    }

    @PostMapping
    @Operation(summary = "Publish a new community notice (Admin)")
    public ResponseEntity<ApiResponse<NoticeDTO>> createNotice(@Valid @RequestBody CreateNoticeRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Notice published successfully", noticeService.createNotice(request)),
                HttpStatus.CREATED
        );
    }
}
