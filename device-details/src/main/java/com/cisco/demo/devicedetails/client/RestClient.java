package com.cisco.demo.devicedetails.client;

import com.cisco.demo.devicedetails.config.ConfigurationService;
import com.cisco.demo.devicedetails.domain.DeviceDetails;
import com.cisco.demo.devicedetails.domain.DeviceDetailsInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestClient{

    private final ConfigurationService configurationService;

    private final RestTemplate restTemplate;

    public static final String SCHEDULED_GET_DEVICE_REQUEST_RECEIVED = "Scheduled Get Device Details Request Received with RequestId [{}]";
    public static final String SCHEDULED_GET_DEVICE_REQUEST_COMPLETED = "Scheduled Get Device Details Request Completed with RequestId [{}]";
    private static final String API_PREFIX = "/api/v1";
    private static final String GET_ALL_DEVICES_ENDPOINT =  API_PREFIX + "/devices";
    private static final String GET_DEVICE_DETAILS_ENDPOINT =  API_PREFIX + "/devices/%s";
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);


    @Scheduled(fixedRate = 300_000)
    public void processRequest() {
        String requestId = UUID.randomUUID().toString();
        try {
            logger.info(SCHEDULED_GET_DEVICE_REQUEST_RECEIVED, requestId);
            List<DeviceDetails> deviceDetailsList = getDevices(requestId);
            if(!CollectionUtils.isEmpty(deviceDetailsList)) {
                deviceDetailsList.parallelStream().forEach(it -> getDeviceDetails(requestId, it.getDeviceId()));
            }
            logger.info(SCHEDULED_GET_DEVICE_REQUEST_COMPLETED, requestId);
        } catch (RuntimeException e){
            logger.error("RequestId [{}] failed. Reason - {}", requestId, e.getMessage());
        }
    }

    List<DeviceDetails> getDevices(String requestId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            String allDevicesUrl = configurationService.getBaseUrl() + GET_ALL_DEVICES_ENDPOINT;
            ResponseEntity<DeviceDetailsInfo> responseEntity = restTemplate.exchange(allDevicesUrl, HttpMethod.GET, httpEntity, DeviceDetailsInfo.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                DeviceDetailsInfo deviceDetailsInfo = responseEntity.getBody();
                return (deviceDetailsInfo != null) ? deviceDetailsInfo.getDeviceDetails() : null;
            }
            logger.error("Received Status code [{}] while retrieving device details Information for RequestId [{}]", responseEntity.getStatusCode(), requestId);
            return null;
        } catch (RestClientException e) {
            logger.error("Error while retrieving device details Information : {} for RequestId [{}]", e.getMessage(), requestId) ;
            throw new IllegalStateException("Error while retrieving device details Information");
        }
    }

    void getDeviceDetails(String requestId, String deviceId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            String DeviceDetailsUrl = configurationService.getBaseUrl() + GET_DEVICE_DETAILS_ENDPOINT;
            final ResponseEntity<DeviceDetails> responseEntity = restTemplate.exchange(String.format(DeviceDetailsUrl, deviceId), HttpMethod.GET, httpEntity, DeviceDetails.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                DeviceDetails deviceDetails = responseEntity.getBody();
                if (deviceDetails != null) {
                    logger.info(deviceDetails.toString());
                }
            } else {
                logger.error("Received Status code [{}] while retrieving device details for RequestId [{}]", responseEntity.getStatusCode(), requestId);
            }
        } catch (RestClientException e) {
            logger.error("Error while retrieving device details : {} for RequestId [{}]", e.getMessage(), requestId);
            throw new IllegalStateException("Error while retrieving device details");
        }
    }
}
