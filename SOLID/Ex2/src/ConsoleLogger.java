public class ConsoleLogger implements Logger {
    public void info(String msg) {
        System.out.println(msg);
    }

    public void infoSameLine(String msg) {
        System.out.print(msg);
    }
}