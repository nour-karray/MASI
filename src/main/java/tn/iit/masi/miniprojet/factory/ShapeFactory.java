package tn.iit.masi.miniprojet.factory;

import javafx.scene.paint.Color;
import tn.iit.masi.miniprojet.decorator.ColoredShapeDecorator;
import tn.iit.masi.miniprojet.model.CircleShape;
import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.model.LineShape;
import tn.iit.masi.miniprojet.model.RectangleShape;
import tn.iit.masi.miniprojet.model.ShapeType;

public class ShapeFactory {

    public DrawableShape createShape(
            ShapeType shapeType,
            double x1,
            double y1,
            double x2,
            double y2,
            Color color,
            double strokeWidth
    ) {
        DrawableShape shape = switch (shapeType) {
            case RECTANGLE -> new RectangleShape(x1, y1, x2, y2, color, strokeWidth);
            case CIRCLE -> new CircleShape(x1, y1, x2, y2, color, strokeWidth);
            case LINE -> new LineShape(x1, y1, x2, y2, color, strokeWidth);
        };
        return new ColoredShapeDecorator(shape, color, strokeWidth);
    }
}
