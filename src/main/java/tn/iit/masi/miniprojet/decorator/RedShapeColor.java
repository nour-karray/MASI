package tn.iit.masi.miniprojet.decorator;

import javafx.scene.paint.Color;

public class RedShapeColor implements ShapeColor {
    @Override
    public String getLabel() {
        return "Rouge";
    }

    @Override
    public Color getColor() {
        return Color.CRIMSON;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
