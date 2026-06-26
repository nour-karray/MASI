package tn.iit.masi.miniprojet.command;

import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.service.DrawingService;

import java.util.List;

public class ClearDrawingCommand implements Command {
    private final DrawingService drawingService;
    private List<DrawableShape> beforeShapes;

    public ClearDrawingCommand(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @Override
    public void execute() {
        beforeShapes = drawingService.copyCurrentShapes();
        drawingService.clearCurrentDrawing();
    }

    @Override
    public void undo() {
        drawingService.replaceCurrentDrawing(beforeShapes, "ANNULER", "Effacement annule");
    }

    @Override
    public String name() {
        return "Effacer le dessin";
    }
}
