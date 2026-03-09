import java.util.*;

public class DeviceRegistry {
    private final java.util.List<Powerable> devices = new ArrayList<>();

    public void add(Powerable d) { devices.add(d); }

    public List<Powerable> getAllPowerable() {
        return devices;
    }

    public <T> T getDeviceWithCapability(Class<T> capability) {
        for (Powerable device : devices) {
            if (capability.isInstance(device)) {
                return capability.cast(device);
            }
        }
        return null;
    }
}
