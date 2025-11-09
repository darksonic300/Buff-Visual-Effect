package com.github.darksonic300.effect_buff;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix4f;

/**
 * Handles the actual drawing of the rising translucent cuboid during the level rendering stage.
 */
@Mod.EventBusSubscriber(modid = EffectBuff.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EffectCuboidRenderer {

    // The time duration of one game tick (1000ms / 20 ticks)
    private static final float MS_PER_TICK = 50.0F;

    private static BlockPos renderPosition = null;

    /**
     * Subscribe to the Render Level Stage event to draw custom 3D geometry.
     * We use AFTER_PARTICLES stage to render translucent geometry over the world.
     */
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES || renderPosition == null) {
            return;
        }

        // Get the PoseStack and the player's camera position
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();

        // Push the matrix state to isolate our transformations
        poseStack.pushPose();

        // 1. Translate to the world position (offset by camera position)
        // This is crucial for rendering at the correct world location
        double x = renderPosition.getX() - camera.getPosition().x;
        double y = renderPosition.getY() - camera.getPosition().y;
        double z = renderPosition.getZ() - camera.getPosition().z;
        poseStack.translate(x, y, z);

        // 2. Call the rendering method
        renderSimpleCube(poseStack, event.getProjectionMatrix());

        // Pop the matrix state to restore previous transformations
        poseStack.popPose();
    }


    private static void renderSimpleCube(PoseStack poseStack, Matrix4f projectionMatrix) {
        // RENDER SETUP
        // Tells the system to prepare for a draw operation.
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Tesselator is used to build the geometry.
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        // Begin drawing quads (4-sided polygons)
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f matrix = poseStack.last().pose();

        float r = 1.0f, g = 0.0f, b = 0.0f, a = 0.5f; // Red, semi-transparent

        // Define the vertices for a 1x1x1 cube at (0, 0, 0)

        // BOTTOM FACE (Y = 0)
        // Vertices are added in counter-clockwise order (facing outward)
        bufferBuilder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 1).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 0, 0, 1).color(r, g, b, a).endVertex();

        // TOP FACE (Y = 1)
        bufferBuilder.vertex(matrix, 0, 1, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 0, 1, 1).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 1, 1).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 1, 0).color(r, g, b, a).endVertex();

        // Side faces (you'll need four more)
        // Example: FRONT FACE (Z = 0)
        bufferBuilder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 0, 1, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 1, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(matrix, 1, 0, 0).color(r, g, b, a).endVertex();

        // ... Add the other three side faces (Back, Left, Right)

        // RENDER EXECUTION
        // This executes the buffer and draws the geometry
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}