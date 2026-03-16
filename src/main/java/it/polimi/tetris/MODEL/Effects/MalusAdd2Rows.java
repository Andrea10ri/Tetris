package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class MalusAdd2Rows extends Effect {

    // Constructor
    public MalusAdd2Rows(String imageUrl) {
        super(EffectType.MALUS, false, imageUrl);

    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        for (TetrisMatch target : targets) {
            target.getTetrisBoard().AddGarbageRow();
            target.getTetrisBoard().AddGarbageRow();
        }
    }
}
