package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class MalusAdd1Row extends Effect {

    // Constructor
    public MalusAdd1Row(String imageUrl) {
        super(EffectType.MALUS, false, imageUrl);

    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        for (TetrisMatch target : targets)
            target.getTetrisBoard().AddGarbageRow();
    }
}
