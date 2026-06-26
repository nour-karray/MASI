package tn.iit.masi.miniprojet.decorator;

import javafx.scene.paint.Color;

public class BlackShapeColor implements ShapeColor {
    @Override
    public String getLabel() {
        return "Noir";
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
