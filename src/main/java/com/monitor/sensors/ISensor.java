package com.monitor.sensors;

public interface ISensor {
    String getId();

    double getValue();

    String getMeasurementUnit();

    boolean isThresholdExceeded();
}
