package com.smartcommunity.dto;

import com.smartcommunity.enums.FeeType;
import com.smartcommunity.enums.PaymentMethod;
import com.smartcommunity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PaymentDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDTO {
        private Long id;
        private Long residentId;
        private String residentName;
        private Long flatId;
        private String flatNumber;
        private String buildingName;
        private BigDecimal amount;
        private FeeType feeType;
        private PaymentStatus paymentStatus;
        private PaymentMethod paymentMethod;
        private String transactionRef;
        private LocalDate dueDate;
        private Instant paidAt;
        private Instant createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePaymentBillRequest {
        @NotNull(message = "Resident ID is required")
        private Long residentId;

        @NotNull(message = "Flat ID is required")
        private Long flatId;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        @NotNull(message = "Fee type is required")
        private FeeType feeType;

        @NotNull(message = "Due date is required")
        private LocalDate dueDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessPaymentSimulatedRequest {
        @NotNull(message = "Payment ID is required")
        private Long paymentId;

        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;
    }
}
