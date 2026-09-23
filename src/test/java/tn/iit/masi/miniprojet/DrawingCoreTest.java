package tn.iit.masi.miniprojet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import tn.iit.masi.miniprojet.command.Command;
import tn.iit.masi.miniprojet.command.CommandManager;
import tn.iit.masi.miniprojet.factory.ShapeFactory;
import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.model.ShapeType;
import tn.iit.masi.miniprojet.persistence.DrawingRepository;
import tn.iit.masi.miniprojet.service.DrawingService;

class DrawingCoreTest {
    private final ShapeFactory factory = new ShapeFactory();

    @Test
    void factoryCreatesRectangleCircleAndLine() {
        assertEquals(ShapeType.RECTANGLE, shape(ShapeType.RECTANGLE).getType());
        assertEquals(ShapeType.CIRCLE, shape(ShapeType.CIRCLE).getType());
        assertEquals(ShapeType.LINE, shape(ShapeType.LINE).getType());
    }

    @Test
    void commandManagerExecutesAndUndoesInLifoOrder() {
        CommandManager manager = new CommandManager();
        StringBuilder events = new StringBuilder();
        manager.executeCommand(command(events, "A"));
        manager.executeCommand(command(events, "B"));
        assertEquals("AB", events.toString());
        assertTrue(manager.undo());
        assertTrue(manager.undo());
        assertFalse(manager.undo());
        assertEquals("ABba", events.toString());
    }

    @Test
    void drawingServiceAddsClearsAndPreservesDrawingOnLoadFailure() {
        DrawableShape rectangle = shape(ShapeType.RECTANGLE);
        DrawingService failingService = new DrawingService(repository(() -> { throw new IllegalStateException("db unavailable"); }));
        failingService.addShape(rectangle);
        assertThrows(IllegalStateException.class, () -> failingService.loadDrawing(1));
        assertEquals(List.of(rectangle), failingService.getCurrentShapes());
        failingService.clearCurrentDrawing();
        assertTrue(failingService.getCurrentShapes().isEmpty());
    }

    @Test
    void drawingServiceReplacesOnlyAfterSuccessfulLoad() {
        DrawableShape existing = shape(ShapeType.LINE);
        DrawableShape loaded = shape(ShapeType.CIRCLE);
        DrawingService service = new DrawingService(repository(() -> List.of(loaded)));
        service.addShape(existing);
        service.loadDrawing(7);
        assertEquals(List.of(loaded), service.getCurrentShapes());
    }

    private DrawableShape shape(ShapeType type) {
        return factory.createShape(type, 1, 2, 3, 4, Color.BLUE, 2);
    }

    private Command command(StringBuilder events, String value) {
        return new Command() {
            public void execute() { events.append(value); }
            public void undo() { events.append(value.toLowerCase()); }
            public String name() { return value; }
        };
    }

    private DrawingRepository repository(Loader loader) {
        return new DrawingRepository(null, null) {
            @Override public List<DrawableShape> loadShapes(long drawingId) { return loader.load(); }
        };
    }

    @FunctionalInterface
    private interface Loader { List<DrawableShape> load(); }
}
