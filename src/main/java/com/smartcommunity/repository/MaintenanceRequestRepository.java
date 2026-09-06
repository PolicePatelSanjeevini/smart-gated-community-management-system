package com.smartcommunity.repository;

import com.smartcommunity.entity.MaintenanceRequest;
import com.smartcommunity.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

    Page<MaintenanceRequest> findByResidentId(Long residentId, Pageable pageable);

    Page<MaintenanceRequest> findByAssignedStaffId(Long staffId, Pageable pageable);

    List<MaintenanceRequest> findByAssignedStaffIdAndStatus(Long staffId, RequestStatus status);

    Page<MaintenanceRequest> findByStatus(RequestStatus status, Pageable pageable);

    long countByStatus(RequestStatus status);

    long countByAssignedStaffIdAndStatus(Long staffId, RequestStatus status);
}
