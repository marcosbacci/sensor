package com.monitor.sensors;

public class TemperatureSensor implements ISensor {
    private String id;
    private double value;
    private final String measurementUnit = "C";
    private final double threshold = 35.0;

    public TemperatureSensor(String id, double value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String getMeasurementUnit() {
        return measurementUnit;
    }

    @Override
    public boolean isThresholdExceeded() {
        return value > threshold;
    }
}
