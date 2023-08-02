package com.cisco.demo.devicedetails.client;

import com.cisco.demo.devicedetails.config.ConfigurationService;
import com.cisco.demo.devicedetails.domain.DeviceDetails;
import com.cisco.demo.devicedetails.domain.DeviceDetailsInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RestClientTest {
    private final String BASE_URL = "http://localhost:8080";
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private ResponseEntity<DeviceDetailsInfo> deviceDetailsInfoEntity;
    @Mock
    private ResponseEntity<DeviceDetails> deviceDetailsEntity;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestClient restClient;

    private final String API_PREFIX = "/api/v1";
    private final String GET_ALL_DEVICES_ENDPOINT =  API_PREFIX + "/devices";
    private final String GET_DEVICE_DETAILS_ENDPOINT =  API_PREFIX + "/devices/%s";
    private final String ID1 = "2ed9a8f5-df2a-4621-b060-38086ec00c7f";
    private final String ID2 = "2ed9a8f5-df2a-4621-b060-38086ec00c9f";


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(configurationService.getBaseUrl()).thenReturn(BASE_URL);
    }

    @Test
    public void testHandleRequest() {
        HttpEntity<String> httpEntity = getStringHttpEntity();
        String allDevicesUrl = BASE_URL + GET_ALL_DEVICES_ENDPOINT;
        String deviceDetailsUrl = BASE_URL + GET_DEVICE_DETAILS_ENDPOINT;

        when(restTemplate.exchange(allDevicesUrl, HttpMethod.GET, httpEntity, DeviceDetailsInfo.class))
                .thenReturn(deviceDetailsInfoEntity);
        when(restTemplate.exchange(String.format(deviceDetailsUrl, ID1), HttpMethod.GET, httpEntity, DeviceDetails.class))
                .thenReturn(deviceDetailsEntity);
        when(restTemplate.exchange(String.format(deviceDetailsUrl, ID2), HttpMethod.GET, httpEntity, DeviceDetails.class))
                .thenReturn(deviceDetailsEntity);
        when(deviceDetailsInfoEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(deviceDetailsInfoEntity.getBody()).thenReturn(buildDeviceDetailsInfo());
        when(deviceDetailsEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(deviceDetailsEntity.getBody()).thenReturn(buildDeviceDetails(ID1));
        restClient.processRequest();
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(), eq(DeviceDetailsInfo.class));
        verify(restTemplate, times(2)).exchange(anyString(), any(HttpMethod.class), any(), eq(DeviceDetails.class));
    }

    private DeviceDetailsInfo buildDeviceDetailsInfo() {
        return DeviceDetailsInfo.builder().deviceDetails(Arrays.asList(buildDeviceDetails(ID1), buildDeviceDetails(ID2))).build();
    }

    private DeviceDetails buildDeviceDetails(String id) {
        return DeviceDetails.builder().deviceId(id).macAddress("macAddress").lastKnownIp("ip").osType("os").hostName("host").build();
    }

    private HttpEntity<String> getStringHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
}
