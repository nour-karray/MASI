package tn.iit.masi.miniprojet.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleShape extends DrawableShape {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    public CircleShape(double x1, double y1, double x2, double y2, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.CIRCLE;
    }

    @Override
    public double getX1() {
        return x1;
    }

    @Override
    public double getY1() {
        return y1;
    }

    @Override
    public double getX2() {
        return x2;
    }

    @Override
    public double getY2() {
        return y2;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double radius = Math.hypot(x2 - x1, y2 - y1);
        gc.strokeOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
    }
}
