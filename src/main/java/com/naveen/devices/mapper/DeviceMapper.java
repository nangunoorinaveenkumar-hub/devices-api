package com.naveen.devices.mapper;

import com.naveen.devices.domain.Device;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device toEntity(DeviceRequest request);

    DeviceResponse toDto(Device device);
}
