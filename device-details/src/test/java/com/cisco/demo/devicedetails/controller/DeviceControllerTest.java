package com.cisco.demo.devicedetails.controller;

import com.cisco.demo.devicedetails.domain.DeviceDetails;
import com.cisco.demo.devicedetails.service.DeviceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    private final String ID1 = "2ed9a8f5-df2a-4621-b060-38086ec00c7f";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testDeviceDetails() {
     when(deviceService.getDeviceInfo(ID1)).thenReturn(buildDeviceDetails(ID1));
        ResponseEntity<DeviceDetails> response = deviceController.getDeviceInfo(ID1);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        verify(deviceService, times(1)).getDeviceInfo(ID1);
    }

    @Test
    public void testDeviceDetails_notFound() {
        when(deviceService.getDeviceInfo(ID1)).thenReturn(null);
        ResponseEntity<DeviceDetails> response = deviceController.getDeviceInfo(ID1);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verify(deviceService, times(1)).getDeviceInfo(ID1);
    }

    private DeviceDetails buildDeviceDetails(String id) {
        return DeviceDetails.builder().deviceId(id).macAddress("macAddress").lastKnownIp("ip").osType("os").hostName("host").build();
    }

}
