package com.cisco.demo.devicedetails.controller;

import com.cisco.demo.devicedetails.domain.DeviceDetails;
import com.cisco.demo.devicedetails.domain.DeviceDetailsInfo;
import com.cisco.demo.devicedetails.service.DeviceService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;
    private static final String DEVICE_ID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    private static final String INVALID_DEVICE_ID = "InValid Device Id format";
    public static final String GET_ALL_DEVICES_REQUEST_RECEIVED = "Get All devices Request Received";
    public static final String GET_ALL_DEVICES_REQUEST_COMPLETED = "Get All devices Request Received";
    public static final String GET_DEVICE_REQUEST_RECEIVED = "Get Device Details Request Received";
    public static final String GET_DEVICE_REQUEST_COMPLETED = "Get Device Details Request Completed";
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);


    @GetMapping
    public ResponseEntity<DeviceDetailsInfo> getAllDevices() {
        logger.info(GET_ALL_DEVICES_REQUEST_RECEIVED);
        List<DeviceDetails> deviceDetails = deviceService.getAllDevices();
        DeviceDetailsInfo deviceDetailsInfo = DeviceDetailsInfo.builder().deviceDetails(deviceDetails).build();
        logger.info(GET_ALL_DEVICES_REQUEST_COMPLETED);
        return new ResponseEntity<>(deviceDetailsInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/{deviceId}")
    public ResponseEntity<DeviceDetails> getDeviceInfo(
            @PathVariable(value = "deviceId") @Pattern(regexp = DEVICE_ID_REGEX, message = INVALID_DEVICE_ID) String deviceId
    ) {
        logger.info(GET_DEVICE_REQUEST_RECEIVED);
        DeviceDetails deviceDetails = deviceService.getDeviceInfo(deviceId);
        if(deviceDetails == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        logger.info(GET_DEVICE_REQUEST_COMPLETED);
        return new ResponseEntity<>(deviceDetails, HttpStatus.OK);
    }
}
