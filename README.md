# Sensor Threshold Monitoring System

This project is a **Sensor Threshold Monitoring System** designed to monitor various sensors via UDP communication. The system processes sensor data and logs alerts when thresholds are exceeded.

---

## Features

- **Extensible Sensor Management**: New sensor types can be added without modifying core logic.
- **Real-Time Monitoring**: Listens for UDP packets and processes sensor data in real-time.
- **Alerting System**: Logs alerts when sensor values exceed their thresholds.

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher.
- **Apache Maven**: For building and running the project.
- **Netcat (or similar UDP client)**: To simulate sensor data transmission.

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com.monitor/
│   │   │   ├── sensors/
│   │   │   │   ├── ISensor.java
│   │   │   │   ├── TemperatureSensor.java
│   │   │   │   ├── HumiditySensor.java
|   |   |   |   ├── SensorFactory.java
│   │   │   ├── services/
│   │   │   │   ├── CentralService.java
│   │   │   │   ├── WarehouseService.java
│   │   ├── Main.java

```

## How to Run

### Step 1: Compile the Project
```bash
mvn clean compile
```

### Step 2: Run the Application
```bash
mvn exec:java
```

### Step 3: Simulate Sensor Data
Use **Netcat** or a similar UDP client to send sensor data.

#### Example Commands:
For a **Temperature Sensor**:
```bash
echo "sensor_id=t1,value=40.0" | nc -u 127.0.0.1 3344
```

For a **Humidity Sensor**:
```bash
echo "sensor_id=h1,value=60.0" | nc -u 127.0.0.1 3355
```

---

## How to Test

### Step 1: Compile the Project
```bash
mvn test
```

---

## How It Works

1. **Sensor Data Processing**:
   - Data packets are received on the mapped ports.
   - Sensors are created dynamically using the `SensorFactory`.

2. **Threshold Checking**:
   - The `CentralService` checks if the sensor value exceeds its threshold.
   - Logs are generated for alerts or normal operations.

---

## Extending the System

To add a new sensor:
1. Create a class implementing `ISensor`.
2. Define the sensor's threshold logic in the `isThresholdExceeded` method.
3. Register the sensor and its logical port in `SensorFactory`:
   ```java
   SensorFactory.registerSensor(<port>, NewSensor::new);
   ```

---

## Example Output

### When a Sensor is Within Threshold:
```
INFO: t1 is within normal range.
```

### When a Sensor Exceeds Threshold:
```
ALERT: t1 exceeded threshold with value: 40.0 C
```

### Dynamic Port Mapping:
```
Listening on port: 3344
Listening on port: 3355
```

---

## Troubleshooting

### Common Issues
- **Port Already in Use**: Ensure no other process is using the port. Use:
  ```bash
  lsof -i :<port>
  ```
  Or kill the process with:
  ```bash
  kill -9 <PID>
  ```

---
