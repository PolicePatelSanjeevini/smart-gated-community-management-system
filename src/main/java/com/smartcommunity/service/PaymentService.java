package com.smartcommunity.service;

import com.smartcommunity.dto.PaymentDTOs.*;
import com.smartcommunity.entity.Flat;
import com.smartcommunity.entity.Payment;
import com.smartcommunity.entity.Resident;
import com.smartcommunity.enums.PaymentStatus;
import com.smartcommunity.exception.BadRequestException;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.FlatRepository;
import com.smartcommunity.repository.PaymentRepository;
import com.smartcommunity.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentDTO createPaymentBill(CreatePaymentBillRequest request) {
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", request.getResidentId()));

        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", request.getFlatId()));

        Payment payment = Payment.builder()
                .resident(resident)
                .flat(flat)
                .amount(request.getAmount())
                .feeType(request.getFeeType())
                .paymentStatus(PaymentStatus.PENDING)
                .dueDate(request.getDueDate())
                .build();

        Payment saved = paymentRepository.save(payment);
        return mapToDTO(saved);
    }

    @Transactional
    public PaymentDTO processSimulatedPayment(ProcessPaymentSimulatedRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", request.getPaymentId()));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException("This payment bill has already been paid.");
        }

        String transactionRef = "TXN-SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(transactionRef);
        payment.setPaidAt(Instant.now());

        Payment saved = paymentRepository.save(payment);
        return mapToDTO(saved);
    }

    public PaymentDTO mapToDTO(Payment p) {
        return PaymentDTO.builder()
                .id(p.getId())
                .residentId(p.getResident().getId())
                .residentName(p.getResident().getUser().getFirstName() + " " + p.getResident().getUser().getLastName())
                .flatId(p.getFlat().getId())
                .flatNumber(p.getFlat().getFlatNumber())
                .buildingName(p.getFlat().getBuilding().getName())
                .amount(p.getAmount())
                .feeType(p.getFeeType())
                .paymentStatus(p.getPaymentStatus())
                .paymentMethod(p.getPaymentMethod())
                .transactionRef(p.getTransactionRef())
                .dueDate(p.getDueDate())
                .paidAt(p.getPaidAt())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
