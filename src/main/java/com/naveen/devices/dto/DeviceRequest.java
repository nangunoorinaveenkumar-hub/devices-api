package com.naveen.devices.dto;

import com.naveen.devices.domain.DeviceState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeviceRequest {
    private String name;
    private String brand;
    private DeviceState state;
}
