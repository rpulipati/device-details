package com.cisco.demo.devicedetails.service;

import com.cisco.demo.devicedetails.domain.DeviceDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DeviceService {

    private static final HashMap<String, DeviceDetails> deviceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        generateData();
    }

    private void generateData() {
        List<String> osList = Arrays.asList("Mac", "Windows", "Linux", "Android");
        List<String> ipList = Arrays.asList("10.10.10.10", "255.255.24.16", "255.255.234.13", "192.168.1.28", "192.168.16.28");
        for (int i = 0; i < 5; i++) {
            String id = UUID.randomUUID().toString();
            String os = osList.get(ThreadLocalRandom.current().nextInt(0, 4));
            String macAddress = generateRandomMac();
            String ip = ipList.get(i);
            deviceMap.put(id,
                    DeviceDetails.builder().deviceId(id).osType(os).hostName("host" + i).lastKnownIp(ip).macAddress(macAddress).build()
            );
        }
    }

    private String generateRandomMac() {
        Random r = new Random();
        StringBuilder mac = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int n = r.nextInt(255);
            mac.append(String.format("%02x-", n));
        }
        return mac.substring(0, mac.length() - 1).toUpperCase();
    }


    public List<DeviceDetails> getAllDevices() {
        return deviceMap.values().stream().toList();
    }

    public DeviceDetails getDeviceInfo(String deviceId) {
        return deviceMap.get(deviceId);
    }
}
