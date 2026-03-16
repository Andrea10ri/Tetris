package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.EffectType;

import java.util.List;

public abstract class Effect {

    //attributes
    protected EffectType effectType; //bonus or malus
    protected boolean isActive;  //it's needed for those effects that persist in time
    protected String imageUrl; // a little image representing the effect

    public Effect(EffectType effectType, boolean isActive, String imageUrl) {
        this.effectType = effectType;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void Tick() {}

    public abstract void Apply(TetrisMatch source, List<TetrisMatch> targets);

}
