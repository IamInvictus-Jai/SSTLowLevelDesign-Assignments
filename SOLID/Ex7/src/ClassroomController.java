public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        InputConnectable pj = reg.getDeviceWithCapability(InputConnectable.class);
        ((Powerable) pj).powerOn();
        pj.connectInput("HDMI-1");

        BrightnessControl lights = reg.getDeviceWithCapability(BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac = reg.getDeviceWithCapability(TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceCapable scan = reg.getDeviceWithCapability(AttendanceCapable.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        // Get all devices that can be powered off
        for (Powerable device : reg.getAllPowerable()) {
            device.powerOff();
        }
    }
}
