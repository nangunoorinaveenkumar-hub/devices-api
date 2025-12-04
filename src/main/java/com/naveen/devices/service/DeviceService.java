package com.naveen.devices.service;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import com.naveen.devices.exception.DeviceNotFoundException;
import com.naveen.devices.converter.DeviceConverter;
import com.naveen.devices.repository.DeviceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;
    private final DeviceConverter deviceConverter;

    public Device createDevice(@Valid final DeviceRequest request) {
        // TODO: Consider enforcing unique combination of 'name' and 'brand' to avoid duplicates.
        // This can be done via a unique constraint in the DB and/or service-level validation.

        final Device device = deviceConverter.toEntity(request);
        final OffsetDateTime now = OffsetDateTime.now();
        device.setCreationTime(now);
        device.setUpdateTime(now);
        return repository.save(device);
    }

    public Device updateDevice(final Long id, final DeviceRequest request) {
        final Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        DeviceValidator.validateUpdate(device, request);

        deviceConverter.updateEntity(request, device);
        device.setUpdateTime(OffsetDateTime.now());

        // TODO: Could add more complex validation here, e.g., prevent state changes under certain conditions
        return repository.save(device);
    }

    public DeviceResponse partialUpdateDevice(final Long id, final DeviceRequest request) {
        final Device device = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        DeviceValidator.validateUpdate(device, request);

        deviceConverter.updateEntity(request, device);
        device.setUpdateTime(OffsetDateTime.now());
        return deviceConverter.toResponse(repository.save(device));
    }

    public void deleteDevice(final Long id) {
        final Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        DeviceValidator.validateDelete(device);

        // TODO: In production, consider soft delete to maintain audit/history
        repository.delete(device);
    }

    @Transactional(readOnly = true)
    public Device getDevice(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Device> getAllDevices() {
        // TODO: Future enhancement: Add pagination and filtering for large datasets
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Device> getDevicesByBrand(final String brand) {
        return repository.findByBrand(brand);
    }

    @Transactional(readOnly = true)
    public List<Device> getDevicesByState(final DeviceState state) {
        return repository.findByState(state);
    }
}
