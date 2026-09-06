package com.smartcommunity.repository;

import com.smartcommunity.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {

    Optional<Resident> findByUserId(Long userId);

    List<Resident> findByFlatId(Long flatId);

    Page<Resident> findByFlatId(Long flatId, Pageable pageable);

    boolean existsByUserIdAndFlatId(Long userId, Long flatId);
}
