package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;
import java.util.List;

//the bottom row of the grid will be removed
public class BonusRemoveARow extends Effect {

    // Constructor
    public BonusRemoveARow(String imageUrl) {
        super(EffectType.BONUS, false, imageUrl);
    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        source.getTetrisBoard().RemoveBottomRow();
    }
}
