package com.naveen.devices.service;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeviceValidatorTest {

    @Test
    void validateDelete_shouldNotThrow_whenDeviceNotInUse() {
        final Device device = Device.builder()
                .state(DeviceState.AVAILABLE)
                .build();

        assertThatCode(() -> DeviceValidator.validateDelete(device))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDelete_shouldThrowIllegalArgument_whenDeviceInUse() {
        final Device device = Device.builder()
                .state(DeviceState.IN_USE)
                .build();

        assertThatThrownBy(() -> DeviceValidator.validateDelete(device))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot delete a device in use");
    }

    @Test
    void validateUpdate_shouldNotThrow_whenDeviceNotInUse() {
        final Device device = Device.builder()
                .name("Phone")
                .brand("Samsung")
                .state(DeviceState.AVAILABLE)
                .build();

        final DeviceRequest request = new DeviceRequest("Phone", "Samsung", DeviceState.AVAILABLE);

        assertThatCode(() -> DeviceValidator.validateUpdate(device, request))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_shouldNotThrow_whenDeviceInUseButNameBrandUnchanged() {
        final Device device = Device.builder()
                .name("Phone")
                .brand("Samsung")
                .state(DeviceState.IN_USE)
                .build();

        final DeviceRequest request = new DeviceRequest("Phone", "Samsung", DeviceState.IN_USE);

        assertThatCode(() -> DeviceValidator.validateUpdate(device, request))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_shouldThrowIllegalState_whenDeviceInUseAndNameChanged() {
        final Device device = Device.builder()
                .name("Phone")
                .brand("Samsung")
                .state(DeviceState.IN_USE)
                .build();

        final DeviceRequest request = new DeviceRequest("NewPhone", "Samsung", DeviceState.IN_USE);

        assertThatThrownBy(() -> DeviceValidator.validateUpdate(device, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot update name/brand of a device in use");
    }

    @Test
    void validateUpdate_shouldThrowIllegalState_whenDeviceInUseAndBrandChanged() {
        final Device device = Device.builder()
                .name("Phone")
                .brand("Samsung")
                .state(DeviceState.IN_USE)
                .build();

        final DeviceRequest request = new DeviceRequest("Phone", "Apple", DeviceState.IN_USE);

        assertThatThrownBy(() -> DeviceValidator.validateUpdate(device, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot update name/brand of a device in use");
    }
}
