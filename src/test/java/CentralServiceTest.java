import com.monitor.sensors.HumiditySensor;
import com.monitor.sensors.TemperatureSensor;
import com.monitor.services.CentralService;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CentralServiceTest {
    private final CentralService centralService = new CentralService();

    @Test
    public void testTemperatureSensorBelowThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TemperatureSensor sensor = new TemperatureSensor("t1", 30.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("INFO: t1 is within normal range."));
    }

    @Test
    public void testTemperatureSensorSameValeuThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TemperatureSensor sensor = new TemperatureSensor("t1", 35.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("INFO: t1 is within normal range."));
    }

    @Test
    public void testTemperatureSensorAboveThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TemperatureSensor sensor = new TemperatureSensor("t1", 36.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("ALERT: t1 exceeded threshold with value: 36.0 C"));
    }

    @Test
    public void testHumiditySensorBelowThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        HumiditySensor sensor = new HumiditySensor("h1", 40.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("INFO: h1 is within normal range."));
    }

    @Test
    public void testHumiditySensorSameValueThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        HumiditySensor sensor = new HumiditySensor("h1", 50.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("INFO: h1 is within normal range."));
    }

    @Test
    public void testHumiditySensorAboveThreshold() {
        // Redirect output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        HumiditySensor sensor = new HumiditySensor("h1", 51.0);
        centralService.processSensorData(sensor);

        assertTrue(out.toString().contains("ALERT: h1 exceeded threshold with value: 51.0 %"));
    }
}
