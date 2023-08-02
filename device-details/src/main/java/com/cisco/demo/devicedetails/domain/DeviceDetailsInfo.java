package com.cisco.demo.devicedetails.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceDetailsInfo {
    private List<DeviceDetails> deviceDetails;
}
