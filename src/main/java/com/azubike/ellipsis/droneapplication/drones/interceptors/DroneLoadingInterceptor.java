package com.azubike.ellipsis.droneapplication.drones.interceptors;

import com.azubike.ellipsis.droneapplication.drones.domain.Drone;
import com.azubike.ellipsis.droneapplication.drones.domain.DroneState;
import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.drones.web.dto.LoadMedicationsDto;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.medications.domian.Medication;
import com.azubike.ellipsis.droneapplication.medications.repository.MedicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class DroneLoadingInterceptor implements HandlerInterceptor {
    private final MedicationRepository medicationRepository;
    private final DroneRepository droneRepository;

    ///drones/{drone_serial_number}/load
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        String droneSerialNumber = request.getParameter("drone_serial_number");
        final Drone drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(() ->
                new NotFoundException(String.format("drone with serial number %s does not exist", droneSerialNumber)));


        if (drone.getState().equals(DroneState.LOADED))
            throw new ConflictException("Drone has reached its maximum capacity");


        if(Integer.parseInt(drone.getBatteryCapacity()) < 25 )
            throw new ConflictException("Drone's battery level is too low");



        LoadMedicationsDto payload = new ObjectMapper().readValue(request.getInputStream(), LoadMedicationsDto.class);


        payload.getMedicationCodes().stream().parallel().forEach(code -> {
            medicationRepository.findMedicationByCode(code).orElseThrow(() -> new NotFoundException(String.format("Medication " +
                    "with code %s does not exist", code)));
        });


        final BigDecimal totalWeight = drone.getMedications().stream().parallel()
                .map(Medication::getWeight).reduce(BigDecimal::add).orElse(new BigDecimal('0'));


        if(totalWeight.compareTo(drone.getWeightLimit()) > 0)
            throw new ConflictException(String.format("Drone's weight limit is %s and cannot accommodate a weight of %s"
                    , drone.getWeightLimit() , totalWeight));


        return HandlerInterceptor.super.preHandle(request, response, handler);


    }
}
