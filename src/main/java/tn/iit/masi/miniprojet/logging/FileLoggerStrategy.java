package tn.iit.masi.miniprojet.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileLoggerStrategy implements LoggerStrategy {
    private final Path logPath;

    public FileLoggerStrategy(Path logPath) {
        this.logPath = logPath;
    }

    @Override
    public void log(String action, String details) {
        String line = LogFormatter.line("FICHIER", action, details) + System.lineSeparator();
        try {
            Files.writeString(logPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'ecrire dans le fichier log", e);
        }
    }
}
