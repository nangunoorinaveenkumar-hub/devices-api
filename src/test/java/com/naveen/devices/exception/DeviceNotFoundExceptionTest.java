package com.naveen.devices.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceNotFoundExceptionTest {

    @Test
    void constructor_shouldSetMessage_whenMessageProvided() {
        final String message = "Device not found";

        final DeviceNotFoundException exception = new DeviceNotFoundException(message);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(message)
                .hasNoCause();
    }

    @Test
    void constructor_shouldAllowNullMessage_whenNullProvided() {
        final DeviceNotFoundException exception = new DeviceNotFoundException(null);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(null)
                .hasNoCause();
    }
}
