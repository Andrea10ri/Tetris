package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class MalusKalamako extends Effect {

    private int durationTime;

    public MalusKalamako(String imageUrl, int durationTime) {
        super(EffectType.MALUS, false, imageUrl);
        this.durationTime = durationTime;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    @Override
    public void Tick() {
        durationTime--;
    }

    public boolean IsExpired() {
        return durationTime <= 0;
    }

    @Override
    public void Apply(TetrisMatch source, List<TetrisMatch> targets) {
        for (TetrisMatch target : targets)
            target.AddEffect(this);
    }
}
