package com.smartcommunity.repository;

import com.smartcommunity.entity.Payment;
import com.smartcommunity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByResidentId(Long residentId, Pageable pageable);

    Optional<Payment> findByTransactionRef(String transactionRef);

    Page<Payment> findByPaymentStatus(PaymentStatus status, Pageable pageable);

    long countByPaymentStatus(PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'PAID'")
    BigDecimal sumTotalCollected();
}
