package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;
import it.polimi.tetris.MODEL.ENUMS.TetronimoType;

/*La forma che cade. Contiene: la matrice di interi con la shape, il tipo (I/J/L/O/S/T/Z),
la posizione corrente sulla board, e hasEffect + l'istanza di Effect associata se speciale.*/

public class Tetronimo {
    private TetronimoType type;
    private int x;
    private int y;
    private int [][] shape;
    private TetronimoColor tetronimoColor;
    private boolean hasEffect;
    private Effect effect;

    public Tetronimo(TetronimoType type, int x, int y, int[][] shape, TetronimoColor tetronimoColor, boolean hasEffect, Effect effect) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.shape = shape;
        this.tetronimoColor = tetronimoColor;
        this.hasEffect = hasEffect;
        this.effect = effect;
    }

    public Tetronimo(TetronimoType type, int[][] shape, TetronimoColor tetronimoColor, boolean hasEffect) {
        this.type = type;
        this.shape = shape;
        this.tetronimoColor = tetronimoColor;
        this.hasEffect = hasEffect;

    }

    public TetronimoType getType() {
        return type;
    }

    public void setType(TetronimoType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TetronimoColor getTetronimoColor() {
        return tetronimoColor;
    }

    public void setTetronimoColor(TetronimoColor tetronimoColor) {
        this.tetronimoColor = tetronimoColor;
    }

    public boolean getHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    //Methods

}
