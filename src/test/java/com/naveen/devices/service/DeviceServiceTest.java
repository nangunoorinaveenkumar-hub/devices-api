package com.naveen.devices.service;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.dto.DeviceResponse;
import com.naveen.devices.exception.DeviceNotFoundException;
import com.naveen.devices.converter.DeviceConverter;
import com.naveen.devices.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository repository;

    @Mock
    private DeviceConverter converter;

    @InjectMocks
    private DeviceService service;

    private Device device;
    private DeviceRequest request;
    private DeviceResponse response;

    @BeforeEach
    void setUp() {
        device = Device.builder()
                        .id(1L)
                        .name("Phone")
                        .brand("Samsung")
                        .state(DeviceState.AVAILABLE)
                        .build();

        request = DeviceRequest.builder()
                               .name("Phone").brand("Samsung")
                               .state(DeviceState.AVAILABLE)
                               .build();

        response = new DeviceResponse(1L, "Phone", "Samsung", "AVAILABLE", OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Test
    void createDevice_shouldSaveAndReturnDevice_whenValidRequest() {
        when(converter.toEntity(request)).thenReturn(device);
        when(repository.save(any(Device.class))).thenReturn(device);

        final Device result = service.createDevice(request);

        assertThat(result).isEqualTo(device);
        verify(repository).save(device);
        verify(converter).toEntity(request);
    }

    @Test
    void updateDevice_shouldUpdateAndReturnDevice_whenDeviceExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(converter).updateEntity(request, device);
        when(repository.save(device)).thenReturn(device);

        final Device result = service.updateDevice(1L, request);

        assertThat(result).isEqualTo(device);
        verify(repository).findById(1L);
        verify(converter).updateEntity(request, device);
        verify(repository).save(device);
    }

    @Test
    void updateDevice_shouldThrowDeviceNotFound_whenDeviceDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDevice(1L, request))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id: 1");
    }

    @Test
    void partialUpdateDevice_shouldUpdateAndReturnResponse_whenDeviceExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(converter).updateEntity(request, device);
        when(repository.save(device)).thenReturn(device);
        when(converter.toResponse(device)).thenReturn(response);

        final DeviceResponse result = service.partialUpdateDevice(1L, request);

        assertThat(result).isEqualTo(response);
        verify(repository).findById(1L);
        verify(converter).updateEntity(request, device);
        verify(converter).toResponse(device);
        verify(repository).save(device);
    }

    @Test
    void partialUpdateDevice_shouldThrowIllegalArgument_whenDeviceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.partialUpdateDevice(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Device not found");
    }

    @Test
    void deleteDevice_shouldDelete_whenDeviceExistsAndNotInUse() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(repository).delete(device);

        service.deleteDevice(1L);

        verify(repository).findById(1L);
        verify(repository).delete(device);
    }

    @Test
    void deleteDevice_shouldThrowDeviceNotFound_whenDeviceDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteDevice(1L))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id: 1");
    }

    @Test
    void getDevice_shouldReturnDevice_whenDeviceExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));

        final Device result = service.getDevice(1L);

        assertThat(result).isEqualTo(device);
    }

    @Test
    void getDevice_shouldThrowDeviceNotFound_whenDeviceDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDevice(1L))
                .isInstanceOf(DeviceNotFoundException.class);
    }

    @Test
    void getAllDevices_shouldReturnList_whenDevicesExist() {
        when(repository.findAll()).thenReturn(List.of(device));

        final List<Device> result = service.getAllDevices();

        assertThat(result).containsExactly(device);
    }

    @Test
    void getDevicesByBrand_shouldReturnList_whenDevicesExist() {
        when(repository.findByBrand("Samsung")).thenReturn(List.of(device));

        final List<Device> result = service.getDevicesByBrand("Samsung");

        assertThat(result).containsExactly(device);
    }

    @Test
    void getDevicesByState_shouldReturnList_whenDevicesExist() {
        when(repository.findByState(DeviceState.AVAILABLE)).thenReturn(List.of(device));

        final List<Device> result = service.getDevicesByState(DeviceState.AVAILABLE);

        assertThat(result).containsExactly(device);
    }
}
