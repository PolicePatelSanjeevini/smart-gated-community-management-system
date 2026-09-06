package com.smartcommunity.service;

import com.smartcommunity.dto.NoticeDTOs.*;
import com.smartcommunity.entity.Notice;
import com.smartcommunity.entity.User;
import com.smartcommunity.enums.TargetAudience;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.NoticeRepository;
import com.smartcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .sorted((n1, n2) -> Boolean.compare(n2.isPinned(), n1.isPinned()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeDTO createNotice(CreateNoticeRequest request) {
        User admin = userRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("User (Admin)", "id", request.getCreatedByAdminId()));

        Notice notice = Notice.builder()
                .createdByAdmin(admin)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .targetAudience(request.getTargetAudience() != null ? request.getTargetAudience() : TargetAudience.ALL)
                .isPinned(request.isPinned())
                .publishDate(Instant.now())
                .build();

        Notice saved = noticeRepository.save(notice);
        return mapToDTO(saved);
    }

    public NoticeDTO mapToDTO(Notice n) {
        return NoticeDTO.builder()
                .id(n.getId())
                .createdByAdminId(n.getCreatedByAdmin().getId())
                .adminName(n.getCreatedByAdmin().getFirstName() + " " + n.getCreatedByAdmin().getLastName())
                .title(n.getTitle())
                .content(n.getContent())
                .category(n.getCategory())
                .targetAudience(n.getTargetAudience())
                .isPinned(n.isPinned())
                .publishDate(n.getPublishDate())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
