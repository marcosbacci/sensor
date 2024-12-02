import com.monitor.sensors.SensorFactory;
import com.monitor.services.CentralService;
import com.monitor.services.WarehouseService;

import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WarehouseServiceTest {

    @Test
    public void testWarehouseServiceHandlesTemperatureSensorData() throws Exception {
        CentralService centralService = new CentralService();
        WarehouseService warehouseService = new WarehouseService(centralService);

        // Start WarehouseService in a separate thread
        new Thread(warehouseService::start).start();

        // Send a UDP packet to the port
        try (DatagramSocket socket = new DatagramSocket()) {
            String message = "sensor_id=t1,value=40.0";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 3344);
            socket.send(packet);
        }

        // Send a UDP packet to the port
        try (DatagramSocket socket = new DatagramSocket()) {
            String message = "sensor_id=h1,value=60.0";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 3355);
            socket.send(packet);
        }

        // Test that the message was processed correctly (log output check is manual
        // here)
        Thread.sleep(1000); // Allow processing time
        assertTrue(SensorFactory.getRegisteredPorts().contains(3344));
        assertTrue(SensorFactory.getRegisteredPorts().contains(3355));
    }
}
