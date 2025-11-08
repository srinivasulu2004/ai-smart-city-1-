package com.smartcity.repository;

import com.smartcity.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAcknowledgedFalse();
    List<Alert> findBySeverity(String severity);
    List<Alert> findByType(String type);
}
