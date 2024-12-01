package com.monitor.services;

import com.monitor.sensors.ISensor;

public class CentralService {
    public void processSensorData(ISensor sensor) {
        try {
            boolean isThresholdExceeded = sensor.isThresholdExceeded();

            if (isThresholdExceeded) {
                System.out.println("ALERT: " + sensor.getId() + " exceeded threshold with value: "
                        + sensor.getValue() + " " + sensor.getMeasurementUnit());
            } else {
                System.out.println("INFO: " + sensor.getId() + " is within normal range.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing sensor data: " + e.getMessage());
        }
    }
}
