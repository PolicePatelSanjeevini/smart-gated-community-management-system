package com.smartcommunity.repository;

import com.smartcommunity.entity.Notice;
import com.smartcommunity.enums.TargetAudience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n WHERE n.targetAudience = 'ALL' OR n.targetAudience = :audience ORDER BY n.isPinned DESC, n.publishDate DESC")
    Page<Notice> findVisibleNoticesForAudience(@Param("audience") TargetAudience audience, Pageable pageable);

    List<Notice> findTop5ByOrderByPublishDateDesc();
}
