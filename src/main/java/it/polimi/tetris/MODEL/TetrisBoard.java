package it.polimi.tetris.MODEL;


public class TetrisBoard {

    //attributes
    private String imageUrl;
    private int Width;
    private int Height;
    private Cell [][] gridTable;
    private Tetronimo currentPiece;
    private Tetronimo nextPiece;

    public TetrisBoard(String imageUrl, int width, int height, Cell[][] gridTable, Tetronimo currentPiece, Tetronimo nextPiece) {
        this.imageUrl = imageUrl;
        Width = width;
        Height = height;
        this.gridTable = gridTable;
        this.currentPiece = currentPiece;
        this.nextPiece = nextPiece;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Tetronimo getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(Tetronimo currentPiece) {
        this.currentPiece = currentPiece;
    }

    public Tetronimo getNextPiece() {
        return nextPiece;
    }

    public void setNextPiece(Tetronimo nextPiece) {
        this.nextPiece = nextPiece;
    }
}
