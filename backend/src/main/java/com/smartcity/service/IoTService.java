package com.smartcity.service;

import com.smartcity.model.IoTDevice;
import com.smartcity.repository.IoTDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class IoTService {
    
    @Autowired
    private IoTDeviceRepository repository;
    
    @Autowired
    private RealTimeDataService realTimeDataService;
    
    private Random random = new Random();
    
    public List<IoTDevice> getAllDevices() {
        return repository.findAll();
    }
    
    public List<IoTDevice> getOnlineDevices() {
        return repository.findByStatus("ONLINE");
    }
    
    public IoTDevice registerDevice(IoTDevice device) {
        device.setLastSeen(LocalDateTime.now());
        IoTDevice saved = repository.save(device);
        realTimeDataService.broadcastIoTDevicesUpdate();
        return saved;
    }
    
    public IoTDevice updateDeviceStatus(String deviceId, String status) {
        IoTDevice device = repository.findByDeviceId(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found: " + deviceId));
        device.setStatus(status);
        device.setLastSeen(LocalDateTime.now());
        IoTDevice saved = repository.save(device);
        realTimeDataService.broadcastIoTDevicesUpdate();
        return saved;
    }
    
    @Scheduled(fixedRate = 15000) // Every 15 seconds
    public void updateDeviceStatuses() {
        List<IoTDevice> devices = repository.findAll();
        for (IoTDevice device : devices) {
            // Simulate device status changes
            if (random.nextDouble() > 0.95) { // 5% chance to go offline
                device.setStatus("OFFLINE");
            } else {
                device.setStatus("ONLINE");
            }
            device.setBatteryLevel(random.nextInt(100));
            device.setSignalStrength(50 + random.nextInt(50));
            device.setLastSeen(LocalDateTime.now());
            repository.save(device);
        }
        realTimeDataService.broadcastIoTDevicesUpdate();
    }
    
    public void initializeSampleDevices() {
        String[] deviceTypes = {"CAMERA", "SENSOR", "TRAFFIC_LIGHT", "ENVIRONMENT_MONITOR"};
        String[] locations = {"Highway I-95", "Central Park", "Downtown", "Financial District", "Residential Area"};
        
        for (int i = 1; i <= 20; i++) {
            IoTDevice device = new IoTDevice();
            device.setDeviceId("DEVICE_" + String.format("%03d", i));
            device.setDeviceType(deviceTypes[random.nextInt(deviceTypes.length)]);
            device.setLocation(locations[random.nextInt(locations.length)]);
            device.setStatus("ONLINE");
            device.setLastSeen(LocalDateTime.now().minusMinutes(random.nextInt(60)));
            device.setBatteryLevel(20 + random.nextInt(80));
            device.setSignalStrength(60 + random.nextInt(40));
            device.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
            device.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
            
            repository.save(device);
        }
        realTimeDataService.broadcastIoTDevicesUpdate();
    }
}
