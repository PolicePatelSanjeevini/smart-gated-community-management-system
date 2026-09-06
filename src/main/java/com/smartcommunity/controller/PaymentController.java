package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.PaymentDTOs.*;
import com.smartcommunity.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Maintenance Charges & Payments", description = "APIs for resident maintenance billing and payment simulation")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get all payment records")
    public ResponseEntity<ApiResponse<List<PaymentDTO>>> getAllPayments() {
        return ResponseEntity.ok(ApiResponse.success("Payment records retrieved", paymentService.getAllPayments()));
    }

    @PostMapping("/bill")
    @Operation(summary = "Generate a new maintenance bill (Admin)")
    public ResponseEntity<ApiResponse<PaymentDTO>> createBill(@Valid @RequestBody CreatePaymentBillRequest request) {
        return new ResponseEntity<>(
                ApiResponse.success("Payment bill generated", paymentService.createPaymentBill(request)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/simulate-pay")
    @Operation(summary = "Simulate payment transaction (Development Payment Flow)")
    public ResponseEntity<ApiResponse<PaymentDTO>> processSimulatedPayment(@Valid @RequestBody ProcessPaymentSimulatedRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Payment processed successfully (Simulator)", paymentService.processSimulatedPayment(request)));
    }
}
