# Ex7 — ISP Refactoring Solution

## Problem Overview

The original code had a "fat interface" called `SmartClassroomDevice` that contained 6 methods. Every device (Projector, Lights, AC, Scanner) was forced to implement ALL methods, even the ones they didn't need. This violated the Interface Segregation Principle (ISP).

## What Was Wrong

1. **Fat Interface**: One interface with too many unrelated methods
2. **Forced Implementation**: Devices had to implement irrelevant methods with dummy code
3. **Unclear Capabilities**: You couldn't tell what a device actually does
4. **Hard to Extend**: Adding new devices meant implementing many useless methods
5. **Misleading Code**: Dummy implementations hide the true behavior

## The Refactoring Solution

### Step 1: Split the Fat Interface

The single `SmartClassroomDevice` interface was broken into 5 small, focused interfaces:

```java
// Power control capability
interface Powerable {
    void powerOn();
    void powerOff();
}

// Brightness control capability
interface BrightnessControl {
    void setBrightness(int pct);
}

// Temperature control capability
interface TemperatureControl {
    void setTemperatureC(int c);
}

// Attendance scanning capability
interface AttendanceCapable {
    int scanAttendance();
}

// Input connection capability
interface InputConnectable {
    void connectInput(String port);
}
```

### Step 2: Devices Implement Only What They Need

Each device now implements only the interfaces relevant to its actual capabilities:

**Projector** - Can be powered and accepts input connections:
```java
public class Projector implements Powerable, InputConnectable {
    // Only implements powerOn(), powerOff(), connectInput()
}
```

**LightsPanel** - Can be powered and brightness adjusted:
```java
public class LightsPanel implements Powerable, BrightnessControl {
    // Only implements powerOn(), powerOff(), setBrightness()
}
```

**AirConditioner** - Can be powered and temperature controlled:
```java
public class AirConditioner implements Powerable, TemperatureControl {
    // Only implements powerOn(), powerOff(), setTemperatureC()
}
```

**AttendanceScanner** - Can be powered and scan attendance:
```java
public class AttendanceScanner implements Powerable, AttendanceCapable {
    // Only implements powerOn(), powerOff(), scanAttendance()
}
```

### Step 3: Capability-Based Device Registry

The `DeviceRegistry` allows looking up devices by their capabilities using reflection:

```java
public <T> T getDeviceWithCapability(Class<T> capability) {
    for (Powerable device : devices) {
        if (capability.isInstance(device)) {
            return capability.cast(device);
        }
    }
    return null;
}
```

### Step 4: Controller Uses Capabilities, Not Concrete Types

The `ClassroomController` doesn't care about specific device types. It asks for capabilities:

```java
// Need something that can connect input? Get it by capability
InputConnectable pj = reg.getDeviceWithCapability(InputConnectable.class);
pj.connectInput("HDMI-1");

// Need brightness control? Get it by capability
BrightnessControl lights = reg.getDeviceWithCapability(BrightnessControl.class);
lights.setBrightness(60);

// Need temperature control? Get it by capability
TemperatureControl ac = reg.getDeviceWithCapability(TemperatureControl.class);
ac.setTemperatureC(24);
```

## Benefits of This Refactoring

1. **No Dummy Code**: Devices only implement methods they actually use
2. **Clear Capabilities**: Interface names clearly show what each device can do
3. **Easy to Extend**: New devices just implement the capabilities they need
4. **Type Safety**: Compiler prevents calling irrelevant methods
5. **Flexible Design**: Controller works with any device that has the needed capability

## ISP Principle Applied

> "Clients should not be forced to depend on interfaces they don't use."

By splitting the fat interface into small, focused interfaces, each device only depends on the methods it actually needs. This makes the code cleaner, safer, and easier to maintain.

## How to Add a New Device

Want to add a SmartBoard? Just implement the capabilities it has:

```java
public class SmartBoard implements Powerable, BrightnessControl, InputConnectable {
    // Implement only these 5 methods, not all 6 from the original fat interface
}
```

No need to implement temperature control or attendance scanning if the SmartBoard doesn't have those features!
