package com.smartcity.service;

import com.smartcity.model.EmergencyIncident;
import com.smartcity.repository.EmergencyIncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class EmergencyService {
    
    @Autowired
    private EmergencyIncidentRepository repository;
    
    @Autowired
    private RealTimeDataService realTimeDataService;
    
    private Random random = new Random();
    
    public List<EmergencyIncident> getActiveIncidents() {
        return repository.findByStatus("ACTIVE");
    }
    
    public List<EmergencyIncident> getAllIncidents() {
        return repository.findAll();
    }
    
    public EmergencyIncident createIncident(EmergencyIncident incident) {
        incident.setReportedAt(LocalDateTime.now());
        incident.setStatus("ACTIVE");
        EmergencyIncident saved = repository.save(incident);
        realTimeDataService.broadcastEmergencyUpdate();
        return saved;
    }
    
    public EmergencyIncident resolveIncident(Long id) {
        EmergencyIncident incident = repository.findById(id).orElseThrow();
        incident.setStatus("RESOLVED");
        incident.setResolvedAt(LocalDateTime.now());
        EmergencyIncident saved = repository.save(incident);
        realTimeDataService.broadcastEmergencyUpdate();
        return saved;
    }
    
    @Scheduled(fixedRate = 60000) // Every minute
    public void generateSampleIncidents() {
        if (random.nextDouble() > 0.8) { // 20% chance to generate incident
            String[] types = {"ACCIDENT", "FIRE", "MEDICAL", "CRIME", "HAZARD"};
            String[] locations = {"Highway I-95", "Main Street", "Central Park", "Downtown", "Residential Area"};
            String[] responseTeams = {"EMS Unit 1", "Fire Truck 2", "Police Car 5", "Hazmat Team"};
            
            EmergencyIncident incident = new EmergencyIncident();
            incident.setIncidentType(types[random.nextInt(types.length)]);
            incident.setLocation(locations[random.nextInt(locations.length)]);
            incident.setSeverity(random.nextInt(5) + 1);
            incident.setDescription(incident.getIncidentType() + " reported at " + incident.getLocation());
            incident.setReportedAt(LocalDateTime.now());
            incident.setStatus("ACTIVE");
            incident.setResponseTeam(responseTeams[random.nextInt(responseTeams.length)]);
            incident.setEtaMinutes(5 + random.nextInt(15));
            incident.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
            incident.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
            
            repository.save(incident);
            realTimeDataService.broadcastEmergencyUpdate();
        }
    }
}
