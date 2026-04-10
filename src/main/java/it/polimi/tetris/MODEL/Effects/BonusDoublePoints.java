package it.polimi.tetris.MODEL.Effects;

import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.Effect;
import it.polimi.tetris.MODEL.TetrisMatch;

import java.util.List;

public class BonusDoublePoints extends Effect {


    private int durationTime;
    // Constructor
    public BonusDoublePoints(String imageUrl, int durationTime) {
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
        if (source.HasActiveEffect(BonusDoublePoints.class)) {
            source.getActiveEffects().removeIf(e -> e instanceof BonusDoublePoints);
        }

        source.AddEffect(this);
    }

}
