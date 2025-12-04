package com.naveen.devices.service;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;

public final class DeviceValidator {

    private DeviceValidator() {}

    public static void validateDelete(final Device device) {
        if (device.getState() == DeviceState.IN_USE) {
            throw new IllegalArgumentException("Cannot delete a device in use");
        }
    }

    public static void validateUpdate(final Device device, final DeviceRequest request) {
        if (device.getState() == DeviceState.IN_USE) {
            final boolean nameChanged = request.getName() != null && !request.getName().equals(device.getName());
            final boolean brandChanged = request.getBrand() != null && !request.getBrand().equals(device.getBrand());

            if (nameChanged || brandChanged) {
                throw new IllegalStateException("Cannot update name/brand of a device in use");
            }
        }
    }
}
