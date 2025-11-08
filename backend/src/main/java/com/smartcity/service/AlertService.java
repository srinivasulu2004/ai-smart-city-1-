package com.smartcity.service;

import com.smartcity.model.Alert;
import com.smartcity.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class AlertService {
    
    @Autowired
    private AlertRepository repository;
    
    @Autowired
    private RealTimeDataService realTimeDataService;
    
    private Random random = new Random();
    
    public List<Alert> getActiveAlerts() {
        return repository.findByAcknowledgedFalse();
    }
    
    public List<Alert> getAllAlerts() {
        return repository.findAll();
    }
    
    public Alert createAlert(Alert alert) {
        alert.setTimestamp(LocalDateTime.now());
        alert.setAcknowledged(false);
        Alert saved = repository.save(alert);
        realTimeDataService.broadcastAlertsUpdate();
        return saved;
    }
    
    public Alert acknowledgeAlert(Long id) {
        Alert alert = repository.findById(id).orElseThrow();
        alert.setAcknowledged(true);
        Alert saved = repository.save(alert);
        realTimeDataService.broadcastAlertsUpdate();
        return saved;
    }
    
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void generateSampleAlerts() {
        if (random.nextDouble() > 0.7) { // 30% chance to generate alert
            String[] types = {"TRAFFIC", "SECURITY", "UTILITY", "EMERGENCY"};
            String[] severities = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
            String[] locations = {"Highway I-95", "Central Park", "Downtown", "Financial District"};
            
            Alert alert = new Alert();
            alert.setType(types[random.nextInt(types.length)]);
            alert.setSeverity(severities[random.nextInt(severities.length)]);
            alert.setLocation(locations[random.nextInt(locations.length)]);
            alert.setMessage("Alert detected in " + alert.getLocation() + " - Type: " + alert.getType());
            alert.setTimestamp(LocalDateTime.now());
            alert.setAcknowledged(false);
            
            repository.save(alert);
            realTimeDataService.broadcastAlertsUpdate();
        }
    }
}
