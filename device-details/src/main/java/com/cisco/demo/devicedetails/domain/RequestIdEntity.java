package com.cisco.demo.devicedetails.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestIdEntity<T> {
    private String requestId;
    private T body;
}
