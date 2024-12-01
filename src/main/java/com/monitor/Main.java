package com.monitor;

import com.monitor.services.CentralService;
import com.monitor.services.WarehouseService;

public class Main {
    public static void main(String[] args) {
        CentralService centralService = new CentralService();
        WarehouseService warehouseService = new WarehouseService(centralService);

        // Start the WarehouseService to listen for sensor data
        new Thread(warehouseService::start).start();

        System.out.println("Warehouse Service is running and listening for sensor data...");
    }
}
