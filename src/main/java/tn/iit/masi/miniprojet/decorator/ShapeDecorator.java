package tn.iit.masi.miniprojet.decorator;

import javafx.scene.canvas.GraphicsContext;
import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.model.ShapeType;

public abstract class ShapeDecorator extends DrawableShape {
    protected final DrawableShape shape;

    protected ShapeDecorator(DrawableShape shape) {
        super(shape.getColor(), shape.getStrokeWidth());
        this.shape = shape;
    }

    @Override
    public ShapeType getType() {
        return shape.getType();
    }

    @Override
    public double getX1() {
        return shape.getX1();
    }

    @Override
    public double getY1() {
        return shape.getY1();
    }

    @Override
    public double getX2() {
        return shape.getX2();
    }

    @Override
    public double getY2() {
        return shape.getY2();
    }

    @Override
    public void draw(GraphicsContext gc) {
        shape.draw(gc);
    }
}
