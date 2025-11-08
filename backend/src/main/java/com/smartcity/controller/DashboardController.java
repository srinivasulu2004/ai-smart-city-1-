package com.smartcity.controller;

import com.smartcity.model.CityOperation;
import com.smartcity.model.TrafficData;
import com.smartcity.service.CityOperationService;
import com.smartcity.service.TrafficService;
import com.smartcity.service.AlertService;
import com.smartcity.service.IoTService;
import com.smartcity.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private CityOperationService operationService;
    
    @Autowired
    private TrafficService trafficService;
    
    @Autowired
    private AlertService alertService;
    
    @Autowired
    private IoTService iotService;
    
    @Autowired
    private EmergencyService emergencyService;
    
    @GetMapping("/overview")
    public Map<String, Object> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        List<CityOperation> activeOps = operationService.getActiveOperations();
        List<CityOperation> criticalOps = operationService.getCriticalOperations();
        List<TrafficData> currentTraffic = trafficService.getCurrentTraffic();
        List<Object> activeAlerts = List.of(alertService.getActiveAlerts());
        List<Object> onlineDevices = List.of(iotService.getOnlineDevices());
        List<Object> activeEmergencies = List.of(emergencyService.getActiveIncidents());
        
        overview.put("totalOperations", activeOps.size());
        overview.put("criticalOperations", criticalOps.size());
        overview.put("activeTrafficLocations", currentTraffic.size());
        overview.put("activeAlerts", activeAlerts.size());
        overview.put("onlineDevices", onlineDevices.size());
        overview.put("activeEmergencies", activeEmergencies.size());
        
        // Calculate average congestion
        double avgCongestion = currentTraffic.stream()
                .mapToInt(TrafficData::getCongestionLevel)
                .average()
                .orElse(0.0);
        overview.put("averageCongestion", avgCongestion);
        
        // Count operations by type
        Map<String, Long> opsByType = activeOps.stream()
                .collect(java.util.stream.Collectors.groupingBy(CityOperation::getOperationType, java.util.stream.Collectors.counting()));
        overview.put("operationsByType", opsByType);
        
        return overview;
    }
    
    @GetMapping("/stats")
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("systemStatus", "OPERATIONAL");
        stats.put("lastUpdate", java.time.LocalDateTime.now().toString());
        stats.put("backendStatus", "ONLINE");
        stats.put("databaseStatus", "ONLINE");
        stats.put("aiServiceStatus", "CONNECTED");
        stats.put("activeConnections", 42);
        stats.put("dataProcessedToday", "2.4TB");
        stats.put("responseTime", "47ms");
        
        return stats;
    }
}
