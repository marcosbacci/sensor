package com.monitor.services;

import com.monitor.sensors.ISensor;
import com.monitor.sensors.SensorFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WarehouseService {
    private CentralService centralService;
    private final Map<Integer, Integer> dynamicPorts = new HashMap<>(); // Map original port to dynamic port

    public WarehouseService(CentralService centralService) {
        this.centralService = centralService;
    }

    public void start() {
        try {
            // Configure the Selector to listen to multiple ports
            Selector selector = Selector.open();

            // Register channels for all ports configured at the factory
            for (Integer port : SensorFactory.getRegisteredPorts()) {
                DatagramChannel channel = DatagramChannel.open();
                channel.socket().bind(new InetSocketAddress(0)); // dynamic port
                int dynamicPort = channel.socket().getLocalPort(); // Sensor port
                dynamicPorts.put(port, dynamicPort);

                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                System.out.println("Listening for logical port " + port + " on dynamic port " + dynamicPort);
            }

            System.out.println("WarehouseService is running and listening for sensor data...");

            // Loop to listen to events in the Selector
            while (true) {
                selector.select(); // Waiting for reading events

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isReadable()) {
                        DatagramChannel channel = (DatagramChannel) key.channel();
                        handleIncomingData(channel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingData(DatagramChannel channel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            InetSocketAddress sourceAddress = (InetSocketAddress) channel.receive(buffer);
            buffer.flip();

            String data = new String(buffer.array(), 0, buffer.limit());
            int dynamicPort = channel.socket().getLocalPort(); // Port used by channel

            // Retrieves the logic gate based on the dynamic gate
            Integer logicalPort = getLogicalPort(dynamicPort);
            if (logicalPort != null) {
                processSensorData(data, logicalPort);
            } else {
                System.err.println("No logical port found for dynamic port: " + dynamicPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSensorData(String data, int logicalPort) {
        try {
            String[] parts = data.split(","); // Exanple: "sensor_id=h1,value=60"
            String id = parts[0].split("=")[1];
            double value = Double.parseDouble(parts[1].split("=")[1]);

            ISensor sensor = SensorFactory.createSensor(logicalPort, id, value);
            centralService.processSensorData(sensor);
        } catch (Exception e) {
            System.err.println("Error processing sensor data: " + e.getMessage());
        }
    }

    private Integer getLogicalPort(int dynamicPort) {
        return dynamicPorts.entrySet().stream()
                .filter(entry -> entry.getValue() == dynamicPort)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
