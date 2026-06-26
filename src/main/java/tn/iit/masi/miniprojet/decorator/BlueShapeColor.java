package tn.iit.masi.miniprojet.decorator;

import javafx.scene.paint.Color;

public class BlueShapeColor implements ShapeColor {
    @Override
    public String getLabel() {
        return "Bleu";
    }

    @Override
    public Color getColor() {
        return Color.DARKBLUE;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
