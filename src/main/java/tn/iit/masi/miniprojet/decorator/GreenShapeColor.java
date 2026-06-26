package tn.iit.masi.miniprojet.decorator;

import javafx.scene.paint.Color;

public class GreenShapeColor implements ShapeColor {
    @Override
    public String getLabel() {
        return "Vert";
    }

    @Override
    public Color getColor() {
        return Color.FORESTGREEN;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
