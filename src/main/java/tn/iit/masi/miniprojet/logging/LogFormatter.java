package tn.iit.masi.miniprojet.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LogFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LogFormatter() {
    }

    public static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String line(String channel, String action, String details) {
        return "[%s] [%s] %s - %s".formatted(now(), channel, action, details);
    }
}
