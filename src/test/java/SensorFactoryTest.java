import org.junit.jupiter.api.Test;

import com.monitor.sensors.HumiditySensor;
import com.monitor.sensors.ISensor;
import com.monitor.sensors.SensorFactory;
import com.monitor.sensors.TemperatureSensor;

import static org.junit.jupiter.api.Assertions.*;

public class SensorFactoryTest {

    @Test
    public void testCreateTemperatureSensor() {
        ISensor sensor = SensorFactory.createSensor(3344, "t1", 25.0);
        assertTrue(sensor instanceof TemperatureSensor);
        assertEquals("t1", sensor.getId());
        assertEquals(25.0, sensor.getValue());
        assertEquals("C", sensor.getMeasurementUnit());
    }

    @Test
    public void testCreateHumiditySensor() {
        ISensor sensor = SensorFactory.createSensor(3355, "h1", 45.0);
        assertTrue(sensor instanceof HumiditySensor);
        assertEquals("h1", sensor.getId());
        assertEquals(45.0, sensor.getValue());
        assertEquals("%", sensor.getMeasurementUnit());
    }

    @Test
    public void testCreateSensorInvalidPort() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> SensorFactory.createSensor(9999, "x1", 50.0));
        assertEquals("No sensor registered for port: 9999", exception.getMessage());
    }
}