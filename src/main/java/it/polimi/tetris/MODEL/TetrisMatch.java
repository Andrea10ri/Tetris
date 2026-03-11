package it.polimi.tetris.MODEL;


import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;
import it.polimi.tetris.MODEL.ENUMS.TetronimoType;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/*Il Tetris di un singolo giocatore.
 Gestisce: spawn del tetromino corrente e del next, movimento e rotazione,
 velocità di caduta e il suo incremento, calcolo punti, la lista activeEffects con il loro tick,
 e lo stato (running/game over).
 È il punto di contatto tra la logica Tetris pura e il sistema di effetti.*/

public class TetrisMatch {

    private TetrisBoard tetrisBoard;
    private Tetronimo currentTetronimo;
    private Tetronimo nextTetronimo;
    private int fallingVelocity;
    private int score;
    private ArrayList<Effect> activeEffects;

    public TetrisMatch(TetrisBoard tetrisBoard, Tetronimo currentTetronimo, Tetronimo nextTetronimo, int fallingVelocity, int score, ArrayList<Effect> activeEffects) {
        this.tetrisBoard = tetrisBoard;
        this.currentTetronimo = currentTetronimo;
        this.nextTetronimo = nextTetronimo;
        this.fallingVelocity = fallingVelocity;
        this.score = score;
        this.activeEffects = activeEffects;
    }

    public TetrisBoard getTetrisBoard() {
        return tetrisBoard;
    }

    public void setTetrisBoard(TetrisBoard tetrisBoard) {
        this.tetrisBoard = tetrisBoard;
    }

    public Tetronimo getCurrentTetronimo() {
        return currentTetronimo;
    }

    public void setCurrentTetronimo(Tetronimo currentTetronimo) {
        this.currentTetronimo = currentTetronimo;
    }

    public Tetronimo getNextTetronimo() {
        return nextTetronimo;
    }

    public void setNextTetronimo(Tetronimo nextTetronimo) {
        this.nextTetronimo = nextTetronimo;
    }

    public int getFallingVelocity() {
        return fallingVelocity;
    }

    public void setFallingVelocity(int fallingVelocity) {
        this.fallingVelocity = fallingVelocity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Effect> getActiveEffects() {
        return activeEffects;
    }

    public void setActiveEffects(ArrayList<Effect> activeEffects) {
        this.activeEffects = activeEffects;
    }

    //Methods
    //Generating the next tetronimo in queue randomly
    public void GenerateNextTetronimo() {

        Random random = new Random();
        int n = random.nextInt(7); //generating the tetronimo type
        int m = random.nextInt(5); //generating if the tetronimo has an effect
        int[][] shape;

        switch (n) {

            //I
            case 0:
                shape = new int[][]{{1, 1, 1, 1},
                                    {0, 0, 0, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.I, shape, TetronimoColor.CYAN, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.I, shape, TetronimoColor.CYAN, false);
                }

                break;

            //O
            case 1:
                shape = new int[][]{{0, 1, 1, 0},
                                    {0, 1, 1, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.O, shape, TetronimoColor.YELLOW, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.O, shape, TetronimoColor.YELLOW, false);
                }

                break;

            //L
            case 2:
                shape = new int[][]{{0, 0, 1, 0},
                                    {1, 1, 1, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.L, shape, TetronimoColor.ORANGE, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.L, shape, TetronimoColor.ORANGE, false);
                }

                break;

            //J
            case 3:
                shape = new int[][]{{1, 0, 0, 0},
                                    {1, 1, 1, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.J, shape, TetronimoColor.BLUE, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.J, shape, TetronimoColor.BLUE, false);
                }

                break;

            //S
            case 4:
                shape = new int[][]{{0, 1, 1, 0},
                                    {1, 1, 0, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.S, shape, TetronimoColor.GREEN, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.S, shape, TetronimoColor.GREEN, false);
                }

                break;

            //Z
            case 5:
                shape = new int[][]{{1, 1, 0, 0},
                                    {0, 1, 1, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.Z, shape, TetronimoColor.RED, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.Z, shape, TetronimoColor.RED, false);
                }

                break;

            //T
            case 6:
                shape = new int[][]{{0, 1, 0, 0},
                                    {1, 1, 1, 0}};

                //with effect
                if (m == 1) {
                    this.nextTetronimo = new Tetronimo(TetronimoType.T, shape, TetronimoColor.PURPLE, true);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.T, shape, TetronimoColor.PURPLE, false);
                }

                break;
        }
    }

}

