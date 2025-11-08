package com.smartcity.controller;

import com.smartcity.model.IoTDevice;
import com.smartcity.service.IoTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/iot")
@CrossOrigin(origins = "*")
public class IoTController {
    
    @Autowired
    private IoTService service;
    
    @GetMapping("/devices")
    public List<IoTDevice> getAllDevices() {
        return service.getAllDevices();
    }
    
    @GetMapping("/devices/online")
    public List<IoTDevice> getOnlineDevices() {
        return service.getOnlineDevices();
    }
    
    @PostMapping("/devices")
    public IoTDevice registerDevice(@RequestBody IoTDevice device) {
        return service.registerDevice(device);
    }
    
    @PutMapping("/devices/{deviceId}/status")
    public IoTDevice updateDeviceStatus(@PathVariable String deviceId, @RequestBody String status) {
        return service.updateDeviceStatus(deviceId, status);
    }
    
    @PostMapping("/devices/initialize")
    public ResponseEntity<String> initializeDevices() {
        service.initializeSampleDevices();
        return ResponseEntity.ok("IoT devices initialized");
    }
}
