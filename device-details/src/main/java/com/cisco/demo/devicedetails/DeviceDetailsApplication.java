package com.cisco.demo.devicedetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableScheduling
public class DeviceDetailsApplication{
	private static final int REST_CONNECTION_TIMEOUT_MSECS = 2000;
	private static final int READ_TIMEOUT_MSECS = 20000;
	public static void main(String[] args) {
		SpringApplication.run(DeviceDetailsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory clientHttpRequestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(REST_CONNECTION_TIMEOUT_MSECS);
		clientHttpRequestFactory.setReadTimeout(READ_TIMEOUT_MSECS);
		return restTemplate;
	}
}
