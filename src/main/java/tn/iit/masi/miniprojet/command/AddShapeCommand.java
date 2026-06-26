package tn.iit.masi.miniprojet.command;

import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.service.DrawingService;

import java.util.List;

public class AddShapeCommand implements Command {
    private final DrawingService drawingService;
    private final DrawableShape shape;
    private List<DrawableShape> beforeShapes;

    public AddShapeCommand(DrawingService drawingService, DrawableShape shape) {
        this.drawingService = drawingService;
        this.shape = shape;
    }

    @Override
    public void execute() {
        beforeShapes = drawingService.copyCurrentShapes();
        drawingService.addShape(shape);
    }

    @Override
    public void undo() {
        drawingService.replaceCurrentDrawing(beforeShapes, "ANNULER", "Ajout de forme annule");
    }

    @Override
    public String name() {
        return "Ajouter une forme";
    }
}
