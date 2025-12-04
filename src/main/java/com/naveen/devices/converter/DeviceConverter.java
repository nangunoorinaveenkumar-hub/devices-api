package com.naveen.devices.converter;

import com.naveen.devices.domain.Device;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import org.springframework.stereotype.Service;

@Service
public class DeviceConverter {

    public Device toEntity(final DeviceRequest request) {
        return Device.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .state(request.getState())
                .build();
    }

    public DeviceResponse toResponse(final Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState().name(),
                device.getCreationTime(),
                device.getUpdateTime()
        );
    }

    public void updateEntity(final DeviceRequest request, final Device device) {
        if (request.getName() != null) device.setName(request.getName());
        if (request.getBrand() != null) device.setBrand(request.getBrand());
        if (request.getState() != null) device.setState(request.getState());
    }
}
