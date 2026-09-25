package com.example.demo.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignCorrelationConfig {

	private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

	private static final String CORRELATION_ID_MDC_KEY = "correlationId";

	@Bean
	RequestInterceptor correlationIdRequestInterceptor() {

		return requestTemplate -> {

			String correlationId = MDC.get(CORRELATION_ID_MDC_KEY);

			if (correlationId != null && !correlationId.isBlank()) {

				requestTemplate.header(CORRELATION_ID_HEADER, correlationId);
			}
		};
	}
}