package com.naveen.devices.controller;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import com.naveen.devices.mapper.DeviceMapper;
import com.naveen.devices.service.DeviceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;

    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody final DeviceRequest request) {
        final Device device = deviceService.createDevice(request);
        return new ResponseEntity<>(deviceMapper.toDto(device), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable final Long id,
            @Valid @RequestBody final DeviceRequest request) {
        final Device updatedDevice = deviceService.updateDevice(id, request);
        return ResponseEntity.ok(deviceMapper.toDto(updatedDevice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        Device device = deviceService.getDevice(id);
        return ResponseEntity.ok(deviceMapper.toDto(device));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        List<DeviceResponse> devices = deviceService.getAllDevices()
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByBrand(@PathVariable final String brand) {
        final List<DeviceResponse> devices = deviceService.getDevicesByBrand(brand)
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByState(@PathVariable final DeviceState state) {
       final List<DeviceResponse> devices = deviceService.getDevicesByState(state)
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable final Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
