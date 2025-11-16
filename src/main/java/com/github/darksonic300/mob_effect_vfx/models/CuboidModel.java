package com.github.darksonic300.mob_effect_vfx.models;

import com.github.darksonic300.mob_effect_vfx.MEVColor;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix4f;

public interface CuboidModel {
    static BufferBuilder engageRender(MEVColor opaque, MEVColor transparency, BufferBuilder bufferBuilder, Matrix4f matrix) {
        return null;
    }
}
