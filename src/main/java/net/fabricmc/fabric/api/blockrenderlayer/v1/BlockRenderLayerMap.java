package net.fabricmc.fabric.api.blockrenderlayer.v1;

import net.fabricmc.fabric.impl.client.NeoForgeClientRegistries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public final class BlockRenderLayerMap {
    public static final BlockRenderLayerMap INSTANCE = new BlockRenderLayerMap();

    private BlockRenderLayerMap() {
    }

    public void putBlock(Block block, RenderType renderType) {
        NeoForgeClientRegistries.registerRenderLayer(block, renderType);
    }
}
