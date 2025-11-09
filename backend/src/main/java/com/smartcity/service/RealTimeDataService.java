package com.smartcity.service;

import com.smartcity.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RealTimeDataService {

    private final CityOperationService operationService;
    private final TrafficService trafficService;
    private final AlertService alertService;
    private final IoTService iotService;
    private final EmergencyService emergencyService;
    private final SimpMessagingTemplate messagingTemplate;

    private final Random random = new Random();

    @Autowired
    public RealTimeDataService(@Lazy CityOperationService operationService,
                              @Lazy TrafficService trafficService,
                              @Lazy AlertService alertService,
                              @Lazy IoTService iotService,
                              @Lazy EmergencyService emergencyService,
                              SimpMessagingTemplate messagingTemplate) {
        this.operationService = operationService;
        this.trafficService = trafficService;
        this.alertService = alertService;
        this.iotService = iotService;
        this.emergencyService = emergencyService;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void broadcastOperationsUpdate() {
        List<CityOperation> operations = operationService.getActiveOperations();
        messagingTemplate.convertAndSend("/topic/operations", operations);
    }

    @Scheduled(fixedRate = 10000)
    public void broadcastTrafficUpdate() {
        List<TrafficData> traffic = trafficService.getCurrentTraffic();
        messagingTemplate.convertAndSend("/topic/traffic", traffic);
    }

    @Scheduled(fixedRate = 15000)
    public void broadcastAlertsUpdate() {
        List<Alert> alerts = alertService.getActiveAlerts();
        messagingTemplate.convertAndSend("/topic/alerts", alerts);
    }

    @Scheduled(fixedRate = 20000)
    public void broadcastIoTDevicesUpdate() {
        List<IoTDevice> devices = iotService.getOnlineDevices();
        messagingTemplate.convertAndSend("/topic/iot_devices", devices);
    }

    @Scheduled(fixedRate = 10000)
    public void broadcastEmergencyUpdate() {
        List<EmergencyIncident> incidents = emergencyService.getActiveIncidents();
        messagingTemplate.convertAndSend("/topic/emergency", incidents);
    }

    @Scheduled(fixedRate = 30000)
    public void broadcastSystemMetrics() {
        Map<String, Object> metrics = generateSystemMetrics();
        messagingTemplate.convertAndSend("/topic/metrics", metrics);
    }

    private Map<String, Object> generateSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("connectedDevices", 1000 + random.nextInt(500));
        metrics.put("dataProcessed", 2.1 + random.nextDouble());
        metrics.put("responseTime", 40 + random.nextInt(20));
        metrics.put("systemUptime", 99.5 + random.nextDouble());
        metrics.put("activeOperations", operationService.getActiveOperations().size());
        metrics.put("activeAlerts", alertService.getActiveAlerts().size());
        metrics.put("onlineDevices", iotService.getOnlineDevices().size());
        metrics.put("timestamp", System.currentTimeMillis());
        return metrics;
    }
}
