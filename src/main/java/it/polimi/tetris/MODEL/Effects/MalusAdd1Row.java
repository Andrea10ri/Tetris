package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;
import java.util.Random;

public class MalusAdd1Row extends Effect {

    // Constructor
    public MalusAdd1Row(String imageUrl) {
        super(EffectType.MALUS, false, imageUrl);

    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        if (targets == null || targets.isEmpty()) return;

        Random random = new Random();
        int n = random.nextInt(targets.size()); // senza +1

        TetrisMatch target = targets.get(n);
        target.getTetrisBoard().AddGarbageRow();
    }
}
