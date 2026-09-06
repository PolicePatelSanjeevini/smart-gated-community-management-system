package com.smartcommunity.repository;

import com.smartcommunity.entity.Complaint;
import com.smartcommunity.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Page<Complaint> findByResidentId(Long residentId, Pageable pageable);

    Page<Complaint> findByAssignedStaffId(Long staffId, Pageable pageable);

    Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);

    long countByStatus(ComplaintStatus status);
}
