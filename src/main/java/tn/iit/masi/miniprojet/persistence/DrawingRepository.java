package tn.iit.masi.miniprojet.persistence;

import javafx.scene.paint.Color;
import tn.iit.masi.miniprojet.factory.ShapeFactory;
import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.model.ShapeType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DrawingRepository {
    private final DatabaseManager databaseManager;
    private final ShapeFactory shapeFactory;

    public DrawingRepository(DatabaseManager databaseManager, ShapeFactory shapeFactory) {
        this.databaseManager = databaseManager;
        this.shapeFactory = shapeFactory;
    }

    public long save(String drawingName, List<DrawableShape> shapes) {
        String insertDrawing = "INSERT INTO drawings(name, created_at) VALUES(?, ?)";
        String insertShape = """
                INSERT INTO shapes(drawing_id, type, x1, y1, x2, y2, color, stroke_width)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);
            long drawingId;
            try (PreparedStatement drawingStatement =
                         connection.prepareStatement(insertDrawing, Statement.RETURN_GENERATED_KEYS)) {
                drawingStatement.setString(1, drawingName);
                drawingStatement.setString(2, LocalDateTime.now().toString());
                drawingStatement.executeUpdate();
                try (ResultSet keys = drawingStatement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new IllegalStateException("ID du dessin introuvable");
                    }
                    drawingId = keys.getLong(1);
                }
            }

            try (PreparedStatement shapeStatement = connection.prepareStatement(insertShape)) {
                for (DrawableShape shape : shapes) {
                    shapeStatement.setLong(1, drawingId);
                    shapeStatement.setString(2, shape.getType().name());
                    shapeStatement.setDouble(3, shape.getX1());
                    shapeStatement.setDouble(4, shape.getY1());
                    shapeStatement.setDouble(5, shape.getX2());
                    shapeStatement.setDouble(6, shape.getY2());
                    shapeStatement.setString(7, shape.getColor().toString());
                    shapeStatement.setDouble(8, shape.getStrokeWidth());
                    shapeStatement.addBatch();
                }
                shapeStatement.executeBatch();
            }

            connection.commit();
            connection.setAutoCommit(true);
            return drawingId;
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible d'enregistrer le dessin", e);
        }
    }

    public List<DrawingSummary> findAllDrawings() {
        String sql = "SELECT id, name FROM drawings ORDER BY id DESC";
        List<DrawingSummary> drawings = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                drawings.add(new DrawingSummary(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return drawings;
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible de lire la liste des dessins", e);
        }
    }

    public List<DrawableShape> loadShapes(long drawingId) {
        String sql = """
                SELECT type, x1, y1, x2, y2, color, stroke_width
                FROM shapes
                WHERE drawing_id = ?
                ORDER BY id ASC
                """;

        List<DrawableShape> shapes = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, drawingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ShapeType type = ShapeType.valueOf(resultSet.getString("type"));
                    shapes.add(shapeFactory.createShape(
                            type,
                            resultSet.getDouble("x1"),
                            resultSet.getDouble("y1"),
                            resultSet.getDouble("x2"),
                            resultSet.getDouble("y2"),
                            Color.valueOf(resultSet.getString("color")),
                            resultSet.getDouble("stroke_width")
                    ));
                }
            }
            return shapes;
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible de charger les formes du dessin", e);
        }
    }
}
