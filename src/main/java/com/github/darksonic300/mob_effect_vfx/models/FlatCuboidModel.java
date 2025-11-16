package com.github.darksonic300.mob_effect_vfx.models;

import com.github.darksonic300.mob_effect_vfx.MEVColor;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import org.joml.Matrix4f;

public class FlatCuboidModel implements CuboidModel {

    private static final float LIGHTEN_FACTOR = 0.3F;

    /**
     * Renders a cuboid with the given color. Scale and other factors are defined in the animation logic.
     * @param poseStack The current PoseStack.
     * @param r Red component (0.0 to 1.0).
     * @param g Green component (0.0 to 1.0).
     * @param b Blue component (0.0 to 1.0).
     * @param a Alpha component (0.0 to 1.0).
     */
    public static void render(PoseStack poseStack, float r, float g, float b, float a, MobEffectCategory category) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f matrix = poseStack.last().pose();

        float la = a - 0.8f;
        la = Mth.clamp(0, la, 1.0f);

        float r_t = Math.min(1.0F, r + LIGHTEN_FACTOR);
        float g_t = Math.min(1.0F, g + LIGHTEN_FACTOR);
        float b_t = Math.min(1.0F, b + LIGHTEN_FACTOR);

        MEVColor opaque = new MEVColor(r, g, b, a);
        MEVColor transparency = new MEVColor(r_t, g_t, b_t, la);

        if(category != MobEffectCategory.HARMFUL){
            bufferBuilder = engageRender(opaque, transparency, bufferBuilder, matrix);
        }else {
            bufferBuilder = engageRender(transparency, opaque, bufferBuilder, matrix);
        }
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    private static BufferBuilder engageRender(MEVColor opaque, MEVColor transparency, BufferBuilder bufferBuilder, Matrix4f matrix) {
        float r = opaque.r();
        float g = opaque.g();
        float b = opaque.b();
        float a = opaque.a();

        float r_t = transparency.r();
        float g_t = transparency.g();
        float b_t = transparency.b();
        float la = transparency.a();

        // TOP FACE (Y = 0)

        bufferBuilder.vertex(matrix, 0.5f, 0, 0.5f).color(r, g, b, la).endVertex();
        bufferBuilder.vertex(matrix, 0, 0, 1).color(r_t, g_t, b_t, a).endVertex();
        bufferBuilder.vertex(matrix, 0, 0, 0).color(r_t, g_t, b_t, a).endVertex();

        bufferBuilder.vertex(matrix, 0.5f, 0, 0.5f).color(r, g, b, la).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 0).color(r_t, g_t, b_t, a).endVertex();
        bufferBuilder.vertex(matrix, 0, 0, 0).color(r_t, g_t, b_t, a).endVertex();

        bufferBuilder.vertex(matrix, 0.5f, 0, 0.5f).color(r, g, b, la).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 0).color(r_t, g_t, b_t, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 1).color(r_t, g_t, b_t, a).endVertex();

        bufferBuilder.vertex(matrix, 0.5f, 0, 0.5f).color(r, g, b, la).endVertex();
        bufferBuilder.vertex(matrix, 0, 0, 1).color(r_t, g_t, b_t, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 1).color(r_t, g_t, b_t, a).endVertex();

        return bufferBuilder;
    }
}