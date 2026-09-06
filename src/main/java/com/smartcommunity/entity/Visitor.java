package com.smartcommunity.entity;

import com.smartcommunity.enums.VisitorStatus;
import com.smartcommunity.enums.VisitorType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "visitors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visitor extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "vehicle_number", length = 30)
    private String vehicleNumber;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "visitor_type", length = 30, nullable = false)
    private VisitorType visitorType;

    @Column(name = "expected_arrival", nullable = false)
    private Instant expectedArrival;

    @Column(name = "entry_time")
    private Instant entryTime;

    @Column(name = "exit_time")
    private Instant exitTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    @Builder.Default
    private VisitorStatus status = VisitorStatus.PRE_REGISTERED;

    @Column(name = "access_code", nullable = false, unique = true, length = 50)
    private String accessCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_guard_id")
    private User verifiedByGuard;
}
