package tn.iit.masi.miniprojet.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final String URL = "jdbc:sqlite:mini_projet.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeSchema();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void initializeSchema() {
        String createDrawingTable = """
                CREATE TABLE IF NOT EXISTS drawings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    created_at TEXT NOT NULL
                )
                """;

        String createShapesTable = """
                CREATE TABLE IF NOT EXISTS shapes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    drawing_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    x1 REAL NOT NULL,
                    y1 REAL NOT NULL,
                    x2 REAL NOT NULL,
                    y2 REAL NOT NULL,
                    color TEXT NOT NULL,
                    stroke_width REAL NOT NULL,
                    FOREIGN KEY (drawing_id) REFERENCES drawings(id)
                )
                """;

        String createLogsTable = """
                CREATE TABLE IF NOT EXISTS logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    action TEXT NOT NULL,
                    details TEXT NOT NULL,
                    created_at TEXT NOT NULL
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createDrawingTable);
            statement.execute(createShapesTable);
            statement.execute(createLogsTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible d'initialiser la base de donnees", e);
        }
    }
}
