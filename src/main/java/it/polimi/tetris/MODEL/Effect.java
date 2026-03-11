package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.EffectType;

public class Effect {

    //attributes
    protected EffectType effectType;
    protected boolean isActive;
    protected String imageUrl;

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
}
