package tn.iit.masi.miniprojet.logging;

public class ConsoleLoggerStrategy implements LoggerStrategy {
    @Override
    public void log(String action, String details) {
        System.out.println(LogFormatter.line("CONSOLE", action, details));
    }
}
