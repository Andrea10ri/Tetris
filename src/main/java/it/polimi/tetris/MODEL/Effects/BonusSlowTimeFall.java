package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class BonusSlowTimeFall extends Effect {

    private int durationTime;
    // Constructor
    public BonusSlowTimeFall(String imageUrl, int durationTime) {
        super(EffectType.BONUS, false, imageUrl);
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

        //se esiste un bonus già attivato dello stesso tipo lo sostituisco
        if (source.HasActiveEffect(BonusSlowTimeFall.class)) {
            source.getActiveEffects().removeIf(e -> e instanceof BonusSlowTimeFall);
            source.setFallingVelocity(source.getFallingVelocity() / 2);
        }



        source.setFallingVelocity(source.getFallingVelocity() * 2);
        source.AddEffect(this);
    }
}
