package com.naveen.devices.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleDeviceNotFound_shouldReturnNotFound_whenExceptionThrown() {
        final DeviceNotFoundException ex = new DeviceNotFoundException("Device missing");

        final ResponseEntity<String> response = handler.handleDeviceNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Device missing");
    }

    @Test
    void handleIllegalArgument_shouldReturnBadRequest_whenExceptionThrown() {
        final IllegalArgumentException ex = new IllegalArgumentException("Invalid value");

        final ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid value");
    }

    @Test
    void handleGeneral_shouldReturnInternalServerError_whenExceptionThrown() {
        final Exception ex = new Exception("Something went wrong");

        final ResponseEntity<String> response = handler.handleGeneral(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Something went wrong");
    }
}
