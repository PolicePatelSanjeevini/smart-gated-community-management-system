package com.smartcommunity.dto;

import com.smartcommunity.enums.NoticeCategory;
import com.smartcommunity.enums.TargetAudience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class NoticeDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeDTO {
        private Long id;
        private Long createdByAdminId;
        private String adminName;
        private String title;
        private String content;
        private NoticeCategory category;
        private TargetAudience targetAudience;
        private boolean isPinned;
        private Instant publishDate;
        private Instant createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateNoticeRequest {
        @NotNull(message = "Admin User ID is required")
        private Long createdByAdminId;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Content is required")
        private String content;

        @NotNull(message = "Category is required")
        private NoticeCategory category;

        private TargetAudience targetAudience;

        private boolean isPinned;
    }
}
