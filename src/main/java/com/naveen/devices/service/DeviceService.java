package com.naveen.devices.service;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.exception.DeviceNotFoundException;
import com.naveen.devices.mapper.DeviceMapper;
import com.naveen.devices.repository.DeviceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;
    private final DeviceMapper mapper;

    public Device createDevice(@Valid final DeviceRequest request) {
        final Device device = mapper.toEntity(request);
        return repository.save(device);
    }

    public Device updateDevice(final Long id, final DeviceRequest request) {
        final Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        if (device.getState() == DeviceState.IN_USE) {
            if (!device.getName().equals(request.getName()) || !device.getBrand().equals(request.getBrand())) {
                throw new IllegalArgumentException("Cannot update name/brand of a device in use");
            }
        }

        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());

        return repository.save(device);
    }

    public Device getDevice(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
    }

    public List<Device> getAllDevices() {
        return repository.findAll();
    }

    public List<Device> getDevicesByBrand(final String brand) {
        return repository.findByBrand(brand);
    }

    public List<Device> getDevicesByState(final DeviceState state) {
        return repository.findByState(state);
    }

    public void deleteDevice(final Long id) {
        final Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        if (device.getState() == DeviceState.IN_USE) {
            throw new IllegalArgumentException("Cannot delete a device in use");
        }

        repository.delete(device);
    }
}
