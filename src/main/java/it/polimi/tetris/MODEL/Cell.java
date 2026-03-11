package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.TetronimoColor;
import it.polimi.tetris.MODEL.ENUMS.CellStatus;

/*Unità minima della griglia. Contiene: se è occupata,
il colore/tipo (normale, garbage da malus, bonus/malus),
e riferimento all'effetto se è una cella speciale.*/
public class Cell {
    //attributes
    private TetronimoColor cellColor;
    private Effect effect;
    private CellStatus status;

    public Cell(TetronimoColor cellColor, Effect effect, CellStatus status) {
        this.cellColor = cellColor;
        this.effect = effect;
        this.status = status;
    }

    public TetronimoColor getCellColor() {
        return cellColor;
    }

    public void setCellColor(TetronimoColor cellColor) {
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
