package com.monitor.sensors;

public class HumiditySensor implements ISensor {
    private String id;
    private double value;
    private final String measurementUnit = "%";
    private final double threshold = 50.0;

    public HumiditySensor(String id, double value) {
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
