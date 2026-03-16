package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class BonusBomb extends Effect {

    // Constructor
    public BonusBomb(String imageUrl) {
        super(EffectType.BONUS, false, imageUrl);
    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        int x = source.getCurrentTetronimo().getX();
        int y = source.getCurrentTetronimo().getY();
        source.getTetrisBoard().ExplodeCells(y, x, 3);
    }
}
