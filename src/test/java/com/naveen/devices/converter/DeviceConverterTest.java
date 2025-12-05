package com.naveen.devices.converter;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceConverterTest {

    private final DeviceConverter converter = new DeviceConverter();

    @Test
    void toEntity_shouldMapFieldsCorrectly_whenValidRequestProvided() {
        final DeviceRequest request = DeviceRequest.builder()
                .name("iPhone")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        final Device device = converter.toEntity(request);

        assertThat(device).isNotNull();
        assertThat(device.getName()).isEqualTo(request.getName());
        assertThat(device.getBrand()).isEqualTo(request.getBrand());
        assertThat(device.getState()).isEqualTo(request.getState());
    }

    @Test
    void toResponse_shouldMapFieldsCorrectly_whenValidDeviceProvided() {
        final OffsetDateTime offsetDateTime = OffsetDateTime.now();
        final Device device = Device.builder()
                .id(10L)
                .name("Watch")
                .brand("Samsung")
                .state(DeviceState.INACTIVE)
                .build();

        device.setCreationTime(offsetDateTime);
        device.setUpdateTime(offsetDateTime);

        final DeviceResponse response = converter.toResponse(device);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Watch");
        assertThat(response.brand()).isEqualTo("Samsung");
        assertThat(response.state()).isEqualTo("INACTIVE");
        assertThat(response.creationTime()).isEqualTo(offsetDateTime);
        assertThat(response.updateTime()).isEqualTo(offsetDateTime);
    }

    @Test
    void updateEntity_shouldUpdateName_whenNameIsPresent() {
        final DeviceRequest request = DeviceRequest.builder().name("Updated Name").build();

        final Device device = Device.builder()
                .name("Old Name")
                .brand("BrandX")
                .state(DeviceState.AVAILABLE)
                .build();

        converter.updateEntity(request, device);

        assertThat(device.getName()).isEqualTo(request.getName());
        assertThat(device.getBrand()).isEqualTo("BrandX");
        assertThat(device.getState()).isEqualTo(DeviceState.AVAILABLE);
    }

    @Test
    void updateEntity_shouldUpdateBrand_whenBrandIsPresent() {
        final DeviceRequest request = DeviceRequest.builder().brand("NewBrand").build();
        final Device device = Device.builder()
                .name("Name")
                .brand("OldBrand")
                .state(DeviceState.IN_USE)
                .build();

        converter.updateEntity(request, device);

        assertThat(device.getBrand()).isEqualTo(request.getBrand());
        assertThat(device.getName()).isEqualTo("Name");
        assertThat(device.getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void updateEntity_shouldUpdateState_whenStateIsPresent() {
        final DeviceRequest request = DeviceRequest.builder().state(DeviceState.INACTIVE).build();

        final Device device = Device.builder()
                .name("Name")
                .brand("BrandX")
                .state(DeviceState.INACTIVE)
                .build();

        converter.updateEntity(request, device);

        assertThat(device.getState()).isEqualTo(request.getState());
    }

    @Test
    void updateEntity_shouldNotChangeAnything_whenRequestFieldsAreNull() {
        final DeviceRequest request = new DeviceRequest();

        final Device device = Device.builder()
                .name("OriginalName")
                .brand("OriginalBrand")
                .state(DeviceState.AVAILABLE)
                .build();

        converter.updateEntity(request, device);

        assertThat(device.getName()).isEqualTo("OriginalName");
        assertThat(device.getBrand()).isEqualTo("OriginalBrand");
        assertThat(device.getState()).isEqualTo(DeviceState.AVAILABLE);
    }
}