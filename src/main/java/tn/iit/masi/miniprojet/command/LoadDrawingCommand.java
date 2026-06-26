package tn.iit.masi.miniprojet.command;

import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.service.DrawingService;

import java.util.List;

public class LoadDrawingCommand implements Command {
    private final DrawingService drawingService;
    private final long drawingId;
    private final String drawingName;
    private List<DrawableShape> beforeShapes;

    public LoadDrawingCommand(DrawingService drawingService, long drawingId, String drawingName) {
        this.drawingService = drawingService;
        this.drawingId = drawingId;
        this.drawingName = drawingName;
    }

    @Override
    public void execute() {
        beforeShapes = drawingService.copyCurrentShapes();
        drawingService.loadDrawing(drawingId, drawingName);
    }

    @Override
    public void undo() {
        drawingService.replaceCurrentDrawing(beforeShapes, "ANNULER", "Ouverture du dessin annulee");
    }

    @Override
    public String name() {
        return "Ouvrir un dessin";
    }
}
