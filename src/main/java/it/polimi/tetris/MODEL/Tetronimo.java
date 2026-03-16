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
    private int xEffect;
    private int yEffect;

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

    public Tetronimo(TetronimoType type, int[][] shape, TetronimoColor tetronimoColor, boolean hasEffect, int xEffect, int yEffect, Effect effect) {
        this.type = type;
        this.shape = shape;
        this.tetronimoColor = tetronimoColor;
        this.hasEffect = hasEffect;
        this.xEffect = xEffect; //coordinata della shape in cui c'è l,effect
        this.yEffect = yEffect;
        this.effect = effect;

    }

    public boolean isHasEffect() {
        return hasEffect;
    }

    public int getxEffect() {
        return xEffect;
    }

    public void setxEffect(int xEffect) {
        this.xEffect = xEffect;
    }

    public int getyEffect() {
        return yEffect;
    }

    public void setyEffect(int yEffect) {
        this.yEffect = yEffect;
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


    //ruota la shape 90° in senso orario
    public void RotateClockwise() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                rotated[c][rows - 1 - r] = shape[r][c];

        this.shape = rotated;
    }

    //ruota la shape 90° in senso antiorario
    public void RotateCounterClockwise() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                rotated[cols - 1 - c][r] = shape[r][c];

        this.shape = rotated;
    }

    //metodo che serve per poter avere una copia del tetronimo su cui fare collision detection
    public Tetronimo Copy() {
        int[][] shapeCopy = new int[shape.length][shape[0].length];
        for (int r = 0; r < shape.length; r++)
            shapeCopy[r] = shape[r].clone();

        return new Tetronimo(type, x, y, shapeCopy, tetronimoColor, hasEffect, effect);
    }
}
