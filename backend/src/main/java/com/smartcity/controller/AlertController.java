package com.smartcity.controller;

import com.smartcity.model.Alert;
import com.smartcity.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertController {
    
    @Autowired
    private AlertService service;
    
    @GetMapping
    public List<Alert> getAllAlerts() {
        return service.getAllAlerts();
    }
    
    @GetMapping("/active")
    public List<Alert> getActiveAlerts() {
        return service.getActiveAlerts();
    }
    
    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return service.createAlert(alert);
    }
    
    @PostMapping("/{id}/acknowledge")
    public Alert acknowledgeAlert(@PathVariable Long id) {
        return service.acknowledgeAlert(id);
    }
}
