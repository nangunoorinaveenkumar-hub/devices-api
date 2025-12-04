package com.naveen.devices.controller;

import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import com.naveen.devices.converter.DeviceConverter;
import com.naveen.devices.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Operations related to device management")
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceConverter deviceConverter;

    @PostMapping
    @Operation(summary = "Create a new device")
    @ApiResponse(responseCode = "201", description = "Device created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody final DeviceRequest request) {
        final DeviceResponse response = deviceConverter.toResponse(deviceService.createDevice(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing device")
    @ApiResponse(responseCode = "200", description = "Device updated successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable final Long id,
            @Valid @RequestBody final DeviceRequest request) {
        final DeviceResponse response = deviceConverter.toResponse(deviceService.updateDevice(id, request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by ID")
    @ApiResponse(responseCode = "200", description = "Device retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        final DeviceResponse response = deviceConverter.toResponse(deviceService.getDevice(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all devices")
    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        List<DeviceResponse> responses = deviceService.getAllDevices()
                .stream()
                .map(deviceConverter::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get devices by brand")
    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    public ResponseEntity<List<DeviceResponse>> getDevicesByBrand(@PathVariable final String brand) {
        List<DeviceResponse> responses = deviceService.getDevicesByBrand(brand)
                .stream()
                .map(deviceConverter::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get devices by state")
    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    public ResponseEntity<List<DeviceResponse>> getDevicesByState(@PathVariable final DeviceState state) {
        final List<DeviceResponse> responses = deviceService.getDevicesByState(state)
                .stream()
                .map(deviceConverter::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a device")
    @ApiResponse(responseCode = "200", description = "Device updated successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public ResponseEntity<DeviceResponse> partialUpdate(
            @PathVariable Long id,
            @RequestBody DeviceRequest request) {
        final DeviceResponse response = deviceService.partialUpdateDevice(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device")
    @ApiResponse(responseCode = "204", description = "Device deleted successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public ResponseEntity<Void> deleteDevice(@PathVariable final Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
