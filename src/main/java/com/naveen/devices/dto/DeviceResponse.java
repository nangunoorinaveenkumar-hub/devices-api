package com.naveen.devices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "Device response DTO")
public record DeviceResponse(
        @Schema(description = "Device ID", example = "1") Long id,
        @Schema(description = "Device name", example = "Device1") String name,
        @Schema(description = "Device brand", example = "BrandA") String brand,
        @Schema(description = "Device state", example = "AVAILABLE") String state,
        @Schema(description = "Creation time", example = "2025-12-04T12:00:00") OffsetDateTime creationTime,
        @Schema(description = "Last update time", example = "2025-12-04T12:30:00") OffsetDateTime updateTime
) {}