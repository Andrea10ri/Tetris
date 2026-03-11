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


}
