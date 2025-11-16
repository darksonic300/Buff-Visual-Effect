package com.github.darksonic300.mob_effect_vfx;

public enum EffectTypes {
    RISING("rising"),
    STATIONARY("stationary"),
    FLAT("flat"),
    EMPTY("");

    private String id;

    EffectTypes(String string) {
        this.id = string;
    }

    public String getId() {
        return id;
    }
}
