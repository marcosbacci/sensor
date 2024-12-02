package com.monitor.services;

import com.monitor.sensors.SensorFactory;
import com.monitor.sensors.ISensor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

public class WarehouseService {
    private CentralService centralService;

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
                channel.socket().bind(new InetSocketAddress(port));
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                System.out.println("Listening on port: " + port);
            }

            System.out.println("WarehouseService is running and listening for sensor data...");

            // Loop para escutar eventos no Selector
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
            int port = channel.socket().getLocalPort(); // Port used by channel

            processSensorData(data, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSensorData(String data, int port) {
        String[] parts = data.split(","); // Example: "sensor_id=h1,value=60"
        String id = parts[0].split("=")[1];
        double value = Double.parseDouble(parts[1].split("=")[1]);

        try {
            ISensor sensor = SensorFactory.createSensor(port, id, value);
            centralService.processSensorData(sensor);
        } catch (Exception e) {
            System.err.println("Error processing sensor data: " + e.getMessage());
        }
    }
}
