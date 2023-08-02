package com.cisco.demo.devicedetails.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ConfigurationService {

    @Value("${base.url}")
    private String baseUrl;
}
