package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class bonusBomb extends Effect {

    private int bombRow;
    private int bombCol;

    public bonusBomb(String imageUrl) {
        super(EffectType.BONUS, false, imageUrl);
    }

    public void SetBombPosition(int row, int col) {
        this.bombRow = row;
        this.bombCol = col;
    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        source.getTetrisBoard().ExplodeCells(bombRow, bombCol, 1);
    }
}
