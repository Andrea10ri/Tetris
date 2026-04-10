package it.polimi.tetris.MODEL;


import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;
import it.polimi.tetris.MODEL.ENUMS.TetronimoType;
import it.polimi.tetris.MODEL.Effects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/*Il Tetris di un singolo giocatore.
 Gestisce: spawn del tetromino corrente e del next, movimento e rotazione,
 velocità di caduta e il suo incremento, calcolo punti, la lista activeEffects con il loro tick,
 e lo stato (running/game over).
 È il punto di contatto tra la logica Tetris pura e il sistema di effetti.*/

public class TetrisMatch {

    private TetrisBoard tetrisBoard; //the board containing the tetris status
    private Tetronimo currentTetronimo; //current descending tetronimo
    private Tetronimo nextTetronimo; //next tetronimo to spawn
    private int fallingVelocity; //multiplier used for managing the down speed
    private int score; //score of the player
    private ArrayList<Effect> activeEffects; //active effects at the moment
    private int tickCounter = 0;
    private Consumer<Integer> onRowsCleared;      //avvisa Game di quante righe sono state cancellate
    private Consumer<Effect> onEffectTriggered;    //avvisa Game di quale effetto è stato triggerato
    private Consumer<TetrisMatch> onGameOver;      //avvisa Game che questo giocatore è in game over

    public TetrisMatch(TetrisBoard tetrisBoard, Tetronimo currentTetronimo, Tetronimo nextTetronimo, int fallingVelocity, int score, ArrayList<Effect> activeEffects) {
        this.tetrisBoard = tetrisBoard;
        this.currentTetronimo = currentTetronimo;
        this.nextTetronimo = nextTetronimo;
        this.fallingVelocity = fallingVelocity;
        this.score = score;
        this.activeEffects = activeEffects;
    }

    public TetrisMatch(TetrisBoard tetrisBoard, int fallingVelocity, int score) {
        this.tetrisBoard = tetrisBoard;
        this.fallingVelocity = fallingVelocity;
        this.score = score;
        this.activeEffects = new ArrayList<>();
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

    public void setOnRowsCleared(Consumer<Integer> callback) {
        this.onRowsCleared = callback;
    }

    public void setOnEffectTriggered(Consumer<Effect> callback) {
        this.onEffectTriggered = callback;
    }

    public void setOnGameOver(Consumer<TetrisMatch> callback) {
        this.onGameOver = callback;
    }

    //Methods
    //Generating the next tetronimo in queue randomly
    public void GenerateNextTetronimo() {

        Random random = new Random();
        int n = random.nextInt(7); //generating the tetronimo type
        int m = random.nextInt(2); //generating if the tetronimo has an effect

        System.out.println("------------------");
        System.out.println(m);
        int[][] shape;

        switch (n) {

            //I
            case 0:
                shape = new int[][]{{1, 1, 1, 1},
                                    {0, 0, 0, 0}};

                //with effect
                if (m == 1) {
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.I, shape, TetronimoColor.CYAN, true, 1, 0, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.O, shape, TetronimoColor.YELLOW, true, 1, 0, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.L, shape, TetronimoColor.ORANGE, true, 2, 1, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.J, shape, TetronimoColor.BLUE, true, 0, 1, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.S, shape, TetronimoColor.GREEN, true, 1, 0, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.Z, shape, TetronimoColor.RED, true, 0, 0, effect);
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
                    Effect effect = GenerateRandomEffect();
                    this.nextTetronimo = new Tetronimo(TetronimoType.T, shape, TetronimoColor.PURPLE, true, 1, 1, effect);
                }

                //without effect
                else {
                    this.nextTetronimo = new Tetronimo(TetronimoType.T, shape, TetronimoColor.PURPLE, false);
                }

                break;
        }
    }


    private Effect GenerateRandomEffect() {
//        Random random = new Random();
//        int n = random.nextInt(10); // 10 effetti totali (per ora)
//
//        switch (n) {
//            case 0: return new BonusRemoveARow("bonusRemoveARow.png");
//            case 1: return new BonusSlowTimeFall("bonusSlowTimeFall.png", 300);
//            case 2: return new BonusDoublePoints("bonusDoublePoints.png", 300);
//            case 3: return new BonusBomb("bonusBomb.png");
//            case 4: return new MalusAdd1Row("malusAdd1Row.png");
//            case 5: return new MalusAdd2Rows("malusAdd2Rows.png");
//            case 6: return new MalusHalvePoints("malusHalvePoints.png");
//            case 7: return new MalusDoubleTetronimo("malusDoubleTetronimo.png");
//            case 8: return new MalusKalamako("malusKalamako.png", 600);
//            case 9: return new MalusReversedControls("malusReversedControls.png", 600);
//            default: return null;
//        }

        Effect effect = new BonusSlowTimeFall("bonusSlowTimeFall.png", 20);
        return effect;
    }

    public void SpawnNextTetronimo() {
        // il next diventa il current
        currentTetronimo = nextTetronimo;

        // posiziona il tetronimo in cima alla board al centro
        currentTetronimo.setX(tetrisBoard.getWidth() / 2 - currentTetronimo.getShape()[0].length / 2);
        currentTetronimo.setY(0);

        // genera il prossimo
        GenerateNextTetronimo();
    }
 //CON WAL KICK
    public void TryRotateClockwise() {
        Tetronimo rotated = currentTetronimo.Copy();
        rotated.RotateClockwise();

        // prova nella posizione attuale
        if (tetrisBoard.IsValidPosition(rotated)) {
            currentTetronimo = rotated;
            return;
        }

        // prova a spostarti di 1 a sinistra
        rotated.setX(rotated.getX() - 1);
        if (tetrisBoard.IsValidPosition(rotated)) {
            currentTetronimo = rotated;
            return;
        }

        // prova a spostarti di 1 a destra
        rotated.setX(rotated.getX() + 2);
        if (tetrisBoard.IsValidPosition(rotated)) {
            currentTetronimo = rotated;
            return;
        }

        // prova a spostarti di 2 a sinistra (per il tetromino I)
        rotated.setX(rotated.getX() - 3);
        if (tetrisBoard.IsValidPosition(rotated)) {
            currentTetronimo = rotated;
        }
    }

    public void TryRotateCounterClockwise() {
        Tetronimo rotated = currentTetronimo.Copy();
        rotated.RotateCounterClockwise();
        if (tetrisBoard.IsValidPosition(rotated))
            currentTetronimo = rotated;

        else
            System.out.println("CounterClockWise rotation fail");
    }

    //Movimento laterale
    public void MoveLeft() {
        if (HasActiveEffect(MalusReversedControls.class))
            MoveRight();
        else {
            Tetronimo moved = currentTetronimo.Copy();
            moved.setX(moved.getX() - 1);
            if (tetrisBoard.IsValidPosition(moved))
                currentTetronimo = moved;

            else
                System.out.println("Left move fail");
        }
    }

    public void MoveRight() {
        if (HasActiveEffect(MalusReversedControls.class))
            MoveLeft();
        else {
            Tetronimo moved = currentTetronimo.Copy();
            moved.setX(moved.getX() + 1);
            if (tetrisBoard.IsValidPosition(moved))
                currentTetronimo = moved;
            else
                System.out.println("Right move fail");
        }
    }

    //Caduta
    public void MoveDown() {
        Tetronimo moved = currentTetronimo.Copy();
        moved.setY(moved.getY() + 1);
        if (tetrisBoard.IsValidPosition(moved))
            currentTetronimo = moved;
        else
            //quando non puó scenere allora piazza il tetronimo
            tetrisBoard.PlaceTetronimo(currentTetronimo);
    }

    public Tetronimo GetGhostTetronimo() {
        Tetronimo ghost = currentTetronimo.Copy();
        ghost.setY(tetrisBoard.GetGhostPieceY(currentTetronimo));
        return ghost;
    }

    public void AddScore(int scored)
    {
        this.score += scored;
    }

    public void HalveScore()
    {
      this.score = this.score /2;
    }

    //metodo che controlla se e quali effetti sono attivi
    public boolean HasActiveEffect(Class<? extends Effect> effectClass) {
        return activeEffects.stream()
                .anyMatch(e -> e.getClass() == effectClass);
    }

    public void AddEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void RemoveEffect(Effect effect) {
        activeEffects.remove(effect);
    }

    public void TickEffects() {
        activeEffects.removeIf(e -> {

            // decrementa ogni secondo reale
            if (tickCounter % 2 == 0)
                e.Tick();

            if (e instanceof BonusDoublePoints) return ((BonusDoublePoints) e).IsExpired();
            if (e instanceof BonusSlowTimeFall && ((BonusSlowTimeFall) e).IsExpired()) {
                this.setFallingVelocity(this.getFallingVelocity() / 2);
                return true;
            }
            if (e instanceof MalusKalamako) return ((MalusKalamako) e).IsExpired();
            if (e instanceof MalusReversedControls) return ((MalusReversedControls) e).IsExpired();
            return false;
        });
    }


    //metodo che gestisce il ciclo di ogni tick, gestisce la caduta, se va bloccato, la pulizia di linee e relativi effetti

    public synchronized void Tick(boolean isACommand) {


      if(!isACommand){  TickEffects();}
        tickCounter++;

        // scende solo ogni fallingVelocity tick
        if (tickCounter % fallingVelocity != 0) return;

        Tetronimo moved = currentTetronimo.Copy();
        moved.setY(moved.getY() + 1);

        if (tetrisBoard.IsValidPosition(moved)) {
            currentTetronimo = moved;
        } else {
            tetrisBoard.PlaceTetronimo(currentTetronimo);
            activeEffects.removeIf(e -> e instanceof MalusDoubleTetronimo);

            int cleared = tetrisBoard.ClearFullRows();
            if (cleared > 0) {
                if (HasActiveEffect(BonusDoublePoints.class))
                    AddScore(cleared * 2);
                else
                    AddScore(cleared);

                onRowsCleared.accept(cleared);

                List<Effect> triggered = tetrisBoard.GetTriggeredEffects();
                for (Effect e : triggered)
                    onEffectTriggered.accept(e);
            }

            SpawnNextTetronimo();

            if (tetrisBoard.IsGameOver())
                onGameOver.accept(this);
        }
    }



}

