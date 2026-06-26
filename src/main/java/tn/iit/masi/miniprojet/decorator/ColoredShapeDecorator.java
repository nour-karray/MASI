package tn.iit.masi.miniprojet.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import tn.iit.masi.miniprojet.model.DrawableShape;

public class ColoredShapeDecorator extends ShapeDecorator {
    private final Color color;
    private final double strokeWidth;

    public ColoredShapeDecorator(DrawableShape shape, Color color, double strokeWidth) {
        super(shape);
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public double getStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        shape.draw(gc);
        gc.restore();
    }
}
