package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUM.CellColor;
import it.polimi.tetris.MODEL.ENUM.CellStatus;

public class Cell {
    //attributes
    private CellColor cellColor;
    private Effect effect;
    private CellStatus status;

    public Cell(CellColor cellColor, Effect effect, CellStatus status) {
        this.cellColor = cellColor;
        this.effect = effect;
        this.status = status;
    }

    public CellColor getCellColor() {
        return cellColor;
    }

    public void setCellColor(CellColor cellColor) {
        this.cellColor = cellColor;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }
}
