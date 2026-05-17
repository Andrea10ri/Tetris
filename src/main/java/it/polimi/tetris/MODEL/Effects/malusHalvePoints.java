package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;
import java.util.Random;

public class malusHalvePoints extends Effect {

    // Constructor
    public malusHalvePoints(String imageUrl) {
        super(EffectType.MALUS, false, imageUrl);
    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {

        if (targets == null || targets.isEmpty()) return;

        Random random = new Random();
        int n = random.nextInt(targets.size());

        TetrisMatch target = targets.get(n);
        target.HalveScore();

    }
}
