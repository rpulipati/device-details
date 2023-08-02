package com.cisco.demo.devicedetails.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetails {

    private String deviceId;
    private String hostName;
    private String osType;
    private String lastKnownIp;
    private String macAddress;
}
