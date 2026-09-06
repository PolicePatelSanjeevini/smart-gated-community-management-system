package com.smartcommunity.repository;

import com.smartcommunity.entity.Visitor;
import com.smartcommunity.enums.VisitorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByAccessCode(String accessCode);

    Page<Visitor> findByResidentId(Long residentId, Pageable pageable);

    Page<Visitor> findByFlatId(Long flatId, Pageable pageable);

    List<Visitor> findByStatus(VisitorStatus status);

    @Query("SELECT v FROM Visitor v WHERE v.expectedArrival >= :startOfDay AND v.expectedArrival < :endOfDay")
    List<Visitor> findExpectedVisitorsToday(@Param("startOfDay") Instant startOfDay, @Param("endOfDay") Instant endOfDay);

    @Query("SELECT COUNT(v) FROM Visitor v WHERE v.entryTime >= :startOfDay AND v.entryTime < :endOfDay")
    long countEntriesToday(@Param("startOfDay") Instant startOfDay, @Param("endOfDay") Instant endOfDay);

    @Query("SELECT COUNT(v) FROM Visitor v WHERE v.exitTime >= :startOfDay AND v.exitTime < :endOfDay")
    long countExitsToday(@Param("startOfDay") Instant startOfDay, @Param("endOfDay") Instant endOfDay);
}
