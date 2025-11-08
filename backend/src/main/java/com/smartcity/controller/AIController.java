package com.smartcity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {
    
    @Value("${ai.service.url:http://ai-service:5000}")
    private String aiServiceUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/predict/traffic")
    public ResponseEntity<?> predictTraffic(@RequestParam String location) {
        try {
            String url = aiServiceUrl + "/predict/traffic?location=" + location;
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> fallback = new HashMap<>();
            fallback.put("prediction", "Moderate traffic expected");
            fallback.put("confidence", "0.85");
            fallback.put("location", location);
            fallback.put("fallback", "true");
            return ResponseEntity.ok(fallback);
        }
    }
    
    @GetMapping("/analyze/trends")
    public ResponseEntity<?> analyzeTrends() {
        try {
            String url = aiServiceUrl + "/analyze/trends";
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> fallback = new HashMap<>();
            fallback.put("trend", "Stable operations with normal fluctuations");
            fallback.put("recommendation", "Continue current monitoring patterns");
            fallback.put("fallback", "true");
            return ResponseEntity.ok(fallback);
        }
    }
    
    @PostMapping("/detect/anomalies")
    public ResponseEntity<?> detectAnomalies(@RequestBody Map<String, Object> sensorData) {
        try {
            String url = aiServiceUrl + "/detect/anomalies";
            String response = restTemplate.postForObject(url, sensorData, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> fallback = new HashMap<>();
            fallback.put("anomalies", "[]");
            fallback.put("message", "Anomaly detection temporarily unavailable");
            fallback.put("fallback", "true");
            return ResponseEntity.ok(fallback);
        }
    }
}
