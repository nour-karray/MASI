package tn.iit.masi.miniprojet.logging;

import tn.iit.masi.miniprojet.persistence.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLoggerStrategy implements LoggerStrategy {
    private final DatabaseManager databaseManager;

    public DatabaseLoggerStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void log(String action, String details) {
        String sql = "INSERT INTO logs(action, details, created_at) VALUES(?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, action);
            statement.setString(2, LogFormatter.line("BASE", action, details));
            statement.setString(3, LogFormatter.now());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible d'enregistrer le log en base", e);
        }
    }
}
