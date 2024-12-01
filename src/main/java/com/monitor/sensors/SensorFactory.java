package com.monitor.sensors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class SensorFactory {
    private static final Map<Integer, BiFunction<String, Double, ISensor>> sensorRegistry = new HashMap<>();

    static {
        // Registering sensors at the factory
        sensorRegistry.put(3344, TemperatureSensor::new);
        sensorRegistry.put(3355, HumiditySensor::new);
    }

    public static ISensor createSensor(int port, String id, double value) {
        BiFunction<String, Double, ISensor> sensorCreator = sensorRegistry.get(port);
        if (sensorCreator == null) {
            throw new IllegalArgumentException("No sensor registered for port: " + port);
        }
        return sensorCreator.apply(id, value);
    }

    // Method for adding new sensors in the future
    public static void registerSensor(int port, BiFunction<String, Double, ISensor> sensorCreator) {
        sensorRegistry.put(port, sensorCreator);
    }

    // Method for reading sensors ports
    public static Set<Integer> getRegisteredPorts() {
        return sensorRegistry.keySet();
    }
}
