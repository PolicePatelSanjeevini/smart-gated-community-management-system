package com.smartcommunity.entity;

import com.smartcommunity.enums.NoticeCategory;
import com.smartcommunity.enums.TargetAudience;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private User createdByAdmin;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50, nullable = false)
    private NoticeCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience", length = 50, nullable = false)
    @Builder.Default
    private TargetAudience targetAudience = TargetAudience.ALL;

    @Column(name = "is_pinned", nullable = false)
    @Builder.Default
    private boolean isPinned = false;

    @Column(name = "publish_date", nullable = false)
    @Builder.Default
    private Instant publishDate = Instant.now();
}
