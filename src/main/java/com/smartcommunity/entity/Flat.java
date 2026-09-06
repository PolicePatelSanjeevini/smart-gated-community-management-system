package com.smartcommunity.entity;

import com.smartcommunity.enums.FlatStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "flats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"building_id", "flat_number"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "flat_number", nullable = false, length = 20)
    private String flatNumber;

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    @Builder.Default
    private FlatStatus status = FlatStatus.VACANT;
}
