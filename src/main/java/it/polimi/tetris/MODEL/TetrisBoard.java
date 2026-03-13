package it.polimi.tetris.MODEL;

/*La griglia di gioco (matrice di Cell).
 Si occupa di: collision detection, piazzare un tetromino, cancellare righe completate,
 aggiungere righe garbage dal basso,
  e restituire quante righe sono state eliminate (serve a Game per la logica delle fasi).*/
public class TetrisBoard {

    //attributes

    private int Width;
    private int Height;
    private Cell [][] gridTable;


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

                //considero solo le celle occupate del tetromino
                if (shape[r][c] == 0)
                    continue;

                int boardRow = tY + r;
                int boardCol = tX + c;

                gridTable[boardRow][boardCol].Occupy(t.getTetronimoColor(), t.getEffect());
            }
        }
    }
}
