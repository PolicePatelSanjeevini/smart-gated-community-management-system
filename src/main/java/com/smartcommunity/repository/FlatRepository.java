package com.smartcommunity.repository;

import com.smartcommunity.entity.Flat;
import com.smartcommunity.enums.FlatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {

    List<Flat> findByBuildingId(Long buildingId);

    Page<Flat> findByBuildingId(Long buildingId, Pageable pageable);

    Optional<Flat> findByBuildingIdAndFlatNumber(Long buildingId, String flatNumber);

    Boolean existsByBuildingIdAndFlatNumber(Long buildingId, String flatNumber);

    long countByStatus(FlatStatus status);
}
