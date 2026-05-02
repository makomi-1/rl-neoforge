package net.fabricmc.fabric.impl.client;

import java.lang.reflect.Method;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

final class ClientRenderLayerSupport {
    private static final Method SET_RENDER_LAYER_RENDER_TYPE = resolveRenderTypeSetter();
    private static final Method SET_RENDER_LAYER_CHUNK_LAYER = resolveChunkLayerSetter();
    private static final Class<?> CHUNK_SECTION_LAYER = resolveChunkSectionLayerClass();

    private ClientRenderLayerSupport() {
    }

    static void setRenderLayer(Block block, RenderType renderType) {
        try {
            if (SET_RENDER_LAYER_RENDER_TYPE != null) {
                SET_RENDER_LAYER_RENDER_TYPE.invoke(null, block, renderType);
                return;
            }
            if (SET_RENDER_LAYER_CHUNK_LAYER != null && CHUNK_SECTION_LAYER != null) {
                Object chunkLayer = resolveChunkLayer(renderType);
                if (chunkLayer != null) {
                    SET_RENDER_LAYER_CHUNK_LAYER.invoke(null, block, chunkLayer);
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static Method resolveRenderTypeSetter() {
        try {
            Class<?> itemBlockRenderTypes = Class.forName("net.minecraft.client.renderer.ItemBlockRenderTypes");
            return itemBlockRenderTypes.getMethod("setRenderLayer", Block.class, RenderType.class);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private static Method resolveChunkLayerSetter() {
        try {
            Class<?> itemBlockRenderTypes = Class.forName("net.minecraft.client.renderer.ItemBlockRenderTypes");
            Class<?> chunkSectionLayer = Class.forName("net.minecraft.client.renderer.chunk.ChunkSectionLayer");
            return itemBlockRenderTypes.getMethod("setRenderLayer", Block.class, chunkSectionLayer);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private static Class<?> resolveChunkSectionLayerClass() {
        try {
            return Class.forName("net.minecraft.client.renderer.chunk.ChunkSectionLayer");
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private static Object resolveChunkLayer(RenderType renderType) {
        if (CHUNK_SECTION_LAYER == null) {
            return null;
        }
        String fieldName = renderType == RenderType.translucent() ? "TRANSLUCENT" : "CUTOUT";
        try {
            return CHUNK_SECTION_LAYER.getField(fieldName).get(null);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }
}
