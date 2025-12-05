package com.naveen.devices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import com.naveen.devices.dto.DeviceRequest;
import com.naveen.devices.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeviceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    @Value("${api.key}")
    private String apiKey;

    @BeforeEach
    void cleanup() {
        deviceRepository.deleteAll();
    }

    @Test
    void createDevice_shouldReturnCreated() throws Exception {
        final DeviceRequest request = DeviceRequest.builder()
                .name("iPhone 15")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        mockMvc.perform(post("/api/devices")
                        .with(apiKeyHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.brand").value("Apple"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"));
    }

    @Test
    void getDevice_shouldReturnDevice() throws Exception {
        final Device request = Device.builder()
                .name("Google Pixel")
                .brand("Google")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Long id = deviceRepository.save(request).getId();

        mockMvc.perform(get("/api/devices/{id}", id)
                        .with(apiKeyHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Google Pixel"))
                .andExpect(jsonPath("$.brand").value("Google"));
    }

    @Test
    void getAllDevices_shouldReturnList() throws Exception {
        final Device device = Device.builder()
                .name("Google Pixel")
                .brand("Google")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Device device1 = Device.builder()
                .name("iPhone 15")
                .brand("Apple")
                .state(DeviceState.IN_USE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        deviceRepository.saveAll(List.of(device, device1));

        mockMvc.perform(get("/api/devices")
                        .with(apiKeyHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateDevice_shouldReturnUpdated() throws Exception {
        final Device device = Device.builder()
                .name("iPhone 15")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Long id = deviceRepository.save(device).getId();

        final DeviceRequest update = DeviceRequest.builder()
                .name("NewName")
                .brand("NewBrand")
                .state(DeviceState.IN_USE)
                .build();

        mockMvc.perform(put("/api/devices/{id}", id)
                        .with(apiKeyHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"))
                .andExpect(jsonPath("$.brand").value("NewBrand"))
                .andExpect(jsonPath("$.state").value("IN_USE"));
    }

    @Test
    void partialUpdateDevice_shouldReturnUpdated() throws Exception {
        final Device device = Device.builder()
                .name("PartialOld")
                .brand("PartialOldBrand")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Long id = deviceRepository.save(device).getId();

        final DeviceRequest patch = DeviceRequest.builder()
                .brand("PartialNewBrand")
                .build();

        mockMvc.perform(patch("/api/devices/{id}", id)
                        .with(apiKeyHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PartialOld"))
                .andExpect(jsonPath("$.brand").value("PartialNewBrand"));
    }

    @Test
    void deleteDevice_shouldReturnNoContent() throws Exception {
        final Device device = Device.builder()
                .name("ToDelete")
                .brand("BrandX")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Long id = deviceRepository.save(device).getId();

        mockMvc.perform(delete("/api/devices/{id}", id)
                        .with(apiKeyHeader()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDevicesByBrand_shouldReturnFiltered() throws Exception {
        final Device device = Device.builder()
                .name("D1")
                .brand("BrandA")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Device device1 = Device.builder()
                .name("D2")
                .brand("BrandB")
                .state(DeviceState.IN_USE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        deviceRepository.saveAll(List.of(device, device1));

        mockMvc.perform(get("/api/devices/brand/{brand}", "BrandA")
                        .with(apiKeyHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].brand").value("BrandA"));
    }

    @Test
    void getDevicesByState_shouldReturnFiltered() throws Exception {
        final Device device = Device.builder()
                .name("D1")
                .brand("BrandA")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        final Device device1 = Device.builder()
                .name("D2")
                .brand("BrandB")
                .state(DeviceState.IN_USE)
                .creationTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .build();

        deviceRepository.saveAll(List.of(device, device1));

        mockMvc.perform(get("/api/devices/state/{state}", "IN_USE")
                        .with(apiKeyHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].state").value("IN_USE"));
    }

    private RequestPostProcessor apiKeyHeader() {
        return request -> {
            request.addHeader("X-API-KEY", apiKey);
            return request;
        };
    }
}