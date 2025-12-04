package com.naveen.devices.controller;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import com.naveen.devices.converter.DeviceConverter;
import com.naveen.devices.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    @Mock
    private DeviceConverter deviceConverter;

    @InjectMocks
    private DeviceController deviceController;

    private Device device;
    private DeviceRequest request;
    private DeviceResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = mock(DeviceRequest.class);
        device = mock(Device.class);
        response = mock(DeviceResponse.class);
    }

    @Test
    void createDevice_should_return_created_device() {
        when(deviceService.createDevice(request)).thenReturn(device);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<DeviceResponse> result = deviceController.createDevice(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void updateDevice_should_update_device() {
        final Long id = 1L;
        when(deviceService.updateDevice(id, request)).thenReturn(device);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<DeviceResponse> result = deviceController.updateDevice(id, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void getDevice_by_id_should_return_device() {
        final Long id = 1L;
        when(deviceService.getDevice(id)).thenReturn(device);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<DeviceResponse> result = deviceController.getDevice(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void getAllDevices_should_return_devices() {
        final List<Device> devices = List.of(device);
        when(deviceService.getAllDevices()).thenReturn(devices);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<List<DeviceResponse>> result = deviceController.getAllDevices();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void getDevicesByBrand_should_return_devices() {
        final String brand = "Samsung";
        final List<Device> devices = List.of(device);
        when(deviceService.getDevicesByBrand(brand)).thenReturn(devices);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<List<DeviceResponse>> result = deviceController.getDevicesByBrand(brand);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void getDevicesByState_should_return_devices() {
        final DeviceState state = DeviceState.AVAILABLE;
        final List<Device> devices = List.of(device);
        when(deviceService.getDevicesByState(state)).thenReturn(devices);
        when(deviceConverter.toResponse(device)).thenReturn(response);

        final ResponseEntity<List<DeviceResponse>> result = deviceController.getDevicesByState(state);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void partialUpdate_should_return_ok() {
        final Long id = 1L;
        when(deviceService.partialUpdateDevice(id, request)).thenReturn(response);

        final ResponseEntity<DeviceResponse> result = deviceController.partialUpdate(id, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void deleteDevice_should_return_no_content() {
        final Long id = 1L;

        final ResponseEntity<Void> result = deviceController.deleteDevice(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
    }
}
