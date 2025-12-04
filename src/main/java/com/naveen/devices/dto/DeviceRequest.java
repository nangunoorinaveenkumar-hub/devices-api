package com.naveen.devices.dto;

import com.naveen.devices.domain.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Device request DTO")
public class DeviceRequest {

    @Schema(description = "Device name", example = "Device1")
    private String name;

    @Schema(description = "Device brand", example = "BrandA")
    private String brand;

    @Schema(description = "Device state", example = "AVAILABLE")
    private DeviceState state;
}