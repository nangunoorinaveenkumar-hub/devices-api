package com.naveen.devices.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ApiKeyFilterTest {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Test
    void doFilterInternal_shouldPass_whenApiKeyValid() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-API-KEY", "test-api-key");
        request.setRequestURI("/api/devices");
        request.setMethod("GET");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        apiKeyFilter.doFilterInternal(request, response, filterChain);

        assertThat(filterChain.isChainCalled()).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    @Test
    void doFilterInternal_shouldReturnUnauthorized_whenApiKeyMissing() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/devices");
        request.setMethod("GET");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        apiKeyFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Invalid or missing API key");
        assertThat(filterChain.isChainCalled()).isFalse();
    }

    @Test
    void doFilterInternal_shouldBypassSwaggerAndActuatorPaths() throws ServletException, IOException {
        String[] paths = {"/swagger-ui/index.html", "/v3/api-docs", "/swagger-ui.html", "/actuator/health"};

        for (String path : paths) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI(path);
            request.setMethod("GET");

            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            apiKeyFilter.doFilterInternal(request, response, filterChain);

            assertThat(filterChain.isChainCalled()).isTrue();
        }
    }

    static class MockHttpServletRequest extends org.springframework.mock.web.MockHttpServletRequest {}

    static class MockHttpServletResponse extends org.springframework.mock.web.MockHttpServletResponse {}

    static class MockFilterChain implements FilterChain {
        private boolean chainCalled = false;

        boolean isChainCalled() {
            return chainCalled;
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) {
            chainCalled = true;
        }
    }
}