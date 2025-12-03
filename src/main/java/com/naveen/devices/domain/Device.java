package com.naveen.devices.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @CreatedDate
    @Column(updatable = false)
    private OffsetDateTime creationTime;

    @LastModifiedDate
    private OffsetDateTime updateTime;
}