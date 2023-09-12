package com.azubike.ellipsis.droneapplication.cron;

import com.azubike.ellipsis.droneapplication.drones.domain.AuditDrone;
import com.azubike.ellipsis.droneapplication.drones.repository.AuditRepository;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatteryLevelAudit {

    private final DroneRepository droneRepository;
    private final AuditRepository auditRepository;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    void checkBatteryLevels() {
        var lowBatteryDrones = droneRepository.findLowBatteryDrones();
        if (lowBatteryDrones.size() > 0) {
            log.debug("Drones with low batteries are present , performing audit log");
            lowBatteryDrones.forEach(drone -> {
                final AuditDrone auditDrone = AuditDrone.builder().droneId(drone.getId()).
                        state(drone.getState())
                        .batteryCapacity(drone.getBatteryCapacity()).build();
                log.debug("Saving drone {} to the audit drone database", auditDrone);
                auditRepository.save(auditDrone);
            });
        } else {
            log.debug("No drones with low battery");
        }
    }

}
