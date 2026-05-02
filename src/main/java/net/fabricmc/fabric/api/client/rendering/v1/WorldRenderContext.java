package net.fabricmc.fabric.api.client.rendering.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fabric 世界渲染上下文兼容对象。
 */
public final class WorldRenderContext {
    private final PoseStack matrixStack;
    private final MultiBufferSource consumers;
    private final Camera camera;
    private final boolean blockOutlines;

    public WorldRenderContext(PoseStack matrixStack, MultiBufferSource consumers, Camera camera) {
        this(matrixStack, consumers, camera, true);
    }

    public WorldRenderContext(PoseStack matrixStack, MultiBufferSource consumers, Camera camera, boolean blockOutlines) {
        this.matrixStack = matrixStack;
        this.consumers = consumers;
        this.camera = camera;
        this.blockOutlines = blockOutlines;
    }

    public PoseStack matrixStack() {
        return matrixStack;
    }

    public MultiBufferSource consumers() {
        return consumers;
    }

    public Camera camera() {
        return camera;
    }

    public boolean blockOutlines() {
        return blockOutlines;
    }

    public double cameraX() {
        return camera == null ? 0.0D : camera.getPosition().x;
    }

    public double cameraY() {
        return camera == null ? 0.0D : camera.getPosition().y;
    }

    public double cameraZ() {
        return camera == null ? 0.0D : camera.getPosition().z;
    }

    /**
     * Fabric 方块描边上下文兼容对象。
     */
    public record BlockOutlineContext(
        BlockPos blockPos,
        BlockState blockState,
        double cameraX,
        double cameraY,
        double cameraZ
    ) {
    }
}
