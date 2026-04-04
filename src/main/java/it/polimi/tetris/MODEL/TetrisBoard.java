package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.CellStatus;
import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*La griglia di gioco (matrice di Cell).
 Si occupa di: collision detection, piazzare un tetromino, cancellare righe completate,
 aggiungere righe garbage dal basso,
  e restituire quante righe sono state eliminate (serve a Game per la logica delle fasi).*/
public class TetrisBoard {

    //attributes
    private int Width;
    private int Height;
    private Cell [][] gridTable;
    private ArrayList<Effect> triggeredEffects = new ArrayList<>();


    public TetrisBoard( int width, int height, Cell[][] gridTable) {
        Width = width;
        Height = height;
        this.gridTable = gridTable;
    }


    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public Cell[][] getGridTable() {
        return gridTable;
    }

    public void setGridTable(Cell[][] gridTable) {
        this.gridTable = gridTable;
    }

    public List<Effect> GetTriggeredEffects() {
        return triggeredEffects;
    }

    //METHODS

    public boolean IsValidPosition(Tetronimo t) {
        int[][] shape = t.getShape();
        int tX = t.getX(); // colonna
        int tY = t.getY(); // riga

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {

                // considero solo le celle occupate del tetromino
                if (shape[r][c] == 0)
                    continue;

                int boardRow = tY + r;
                int boardCol = tX + c;

                // fuori dai bordi
                if (boardCol < 0 || boardCol >= Width)
                    return false;
                if (boardRow >= Height)
                    return false;

                // sopra la board è ok (il tetromino sta spawnando)
                if (boardRow < 0)
                    continue;

                // cella già occupata
                if (!gridTable[boardRow][boardCol].IsEmpty())
                    return false;
            }
        }
        return true;
    }

    public void PlaceTetronimo(Tetronimo t) {
        int[][] shape = t.getShape();
        int tX = t.getX();
        int tY = t.getY();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {

                if (shape[r][c] == 0) continue;

                int boardRow = tY + r;
                int boardCol = tX + c;

                // se è la cella speciale passa l'effetto, altrimenti null
                if (t.getHasEffect() && r == t.getyEffect() && c == t.getxEffect())
                    gridTable[boardRow][boardCol].Occupy(t.getTetronimoColor(), t.getEffect());
                else
                    gridTable[boardRow][boardCol].Occupy(t.getTetronimoColor(), null);
            }
        }
    }

    // shifta tutte le righe verso l'alto di 1
    public void AddGarbageRow() {

        for (int r = 0; r < Height - 1; r++) {
            for (int c = 0; c < Width; c++) {
                gridTable[r][c] = gridTable[r + 1][c];
            }
        }

        // costruisce la nuova riga garbage in fondo con un buco casuale
        Random random = new Random();
        int holeCol = random.nextInt(Width); // colonna che resta vuota

        for (int c = 0; c < Width; c++) {
            if (c == holeCol)
                gridTable[Height - 1][c] = new Cell(null, null, CellStatus.EMPTY);
            else
                gridTable[Height - 1][c] = new Cell(TetronimoColor.GREY, null, CellStatus.OCCUPIED);
        }
    }

    public void RemoveBottomRow() {
        // shifta tutte le righe verso il basso di 1
        for (int r = Height - 1; r > 0; r--) {
            for (int c = 0; c < Width; c++) {
                gridTable[r][c] = gridTable[r - 1][c];
            }
        }
        // la riga 0 diventa vuota
        for (int c = 0; c < Width; c++) {
            gridTable[0][c] = new Cell(null, null, CellStatus.EMPTY);
        }
    }

    public int ClearFullRows() {
        triggeredEffects.clear(); // pulisce gli effetti triggerati dal turno precedente

        int clearedRows = 0;

        for (int r = Height - 1; r >= 0; r--) {

            // controlla se la riga è piena
            boolean isFull = true;
            for (int c = 0; c < Width; c++) {
                if (gridTable[r][c].IsEmpty()) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                // prima di cancellare la riga, salva gli effetti trovati nelle celle
                for (int c = 0; c < Width; c++) {
                    if (gridTable[r][c].getEffect() != null)
                        triggeredEffects.add(gridTable[r][c].getEffect());
                }

                // shifta tutte le righe sopra verso il basso di 1
                for (int row = r; row > 0; row--) {
                    for (int c = 0; c < Width; c++) {
                        gridTable[row][c] = gridTable[row - 1][c];
                    }
                }

                // la riga 0 diventa vuota
                for (int c = 0; c < Width; c++) {
                    gridTable[0][c] = new Cell(null, null, CellStatus.EMPTY);
                }

                clearedRows++;
                r++; // ricontrolla la stessa riga perché è scesa una nuova
            }
        }
        return clearedRows;
    }

    //metodo usato per l'effetto bomb
    public void ExplodeCells(int row, int col, int radius) {
        for (int r = row - radius; r <= row + radius; r++) {
            for (int c = col - radius; c <= col + radius; c++) {
                // controlla che la cella sia dentro i bordi della board
                if (r >= 0 && r < Height && c >= 0 && c < Width)
                    gridTable[r][c].Clear();
            }
        }
    }

    public boolean IsGameOver() {
        // controlla se la riga 0 ha almeno una cella occupata
        for (int c = 0; c < Width; c++) {
            if (!gridTable[0][c].IsEmpty())
                return true;
        }
        return false;
    }

    //metodo che ritorna la posizione di dove potrebbe cadere il tetronimo
    public int GetGhostPieceY(Tetronimo t) {
        Tetronimo ghost = t.Copy();

        // scende finché può
        while (IsValidPosition(ghost)) {
            ghost.setY(ghost.getY() + 1);
        }

        // torna su di 1 perché l'ultima posizione era invalida
        return ghost.getY() - 1;
    }
}
