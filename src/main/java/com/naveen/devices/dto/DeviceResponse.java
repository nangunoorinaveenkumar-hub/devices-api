package com.naveen.devices.dto;

import com.naveen.devices.domain.DeviceState;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record DeviceResponse(
        Long id,
        String name,
        String brand,
        DeviceState state,
        OffsetDateTime creationTime,
        OffsetDateTime updateTime
) {}
