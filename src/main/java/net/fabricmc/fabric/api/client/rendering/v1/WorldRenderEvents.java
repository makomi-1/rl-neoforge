package net.fabricmc.fabric.api.client.rendering.v1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Fabric 世界渲染事件兼容层。
 */
public final class WorldRenderEvents {
    public static final BlockOutlineEvent BLOCK_OUTLINE = new BlockOutlineEvent();
    public static final StageEvent AFTER_TRANSLUCENT = new StageEvent();
    public static final StageEvent LAST = new StageEvent();

    private WorldRenderEvents() {
    }

    public static boolean fireBlockOutline(
        WorldRenderContext context,
        WorldRenderContext.BlockOutlineContext blockOutlineContext
    ) {
        boolean keepVanilla = true;
        for (BlockOutline callback : BLOCK_OUTLINE.listeners()) {
            keepVanilla &= callback.onBlockOutline(context, blockOutlineContext);
        }
        return keepVanilla;
    }

    public static void fireAfterTranslucent(WorldRenderContext context) {
        AFTER_TRANSLUCENT.fire(context);
    }

    public static void fireLast(WorldRenderContext context) {
        LAST.fire(context);
    }

    @FunctionalInterface
    public interface BlockOutline {
        boolean onBlockOutline(WorldRenderContext context, WorldRenderContext.BlockOutlineContext blockOutlineContext);
    }

    @FunctionalInterface
    public interface Stage {
        void onRender(WorldRenderContext context);
    }

    public static final class BlockOutlineEvent {
        private final List<BlockOutline> listeners = new CopyOnWriteArrayList<>();

        public void register(BlockOutline listener) {
            listeners.add(listener);
        }

        private List<BlockOutline> listeners() {
            return listeners;
        }
    }

    public static final class StageEvent {
        private final List<Stage> listeners = new CopyOnWriteArrayList<>();

        public void register(Stage listener) {
            listeners.add(listener);
        }

        private void fire(WorldRenderContext context) {
            for (Stage listener : listeners) {
                listener.onRender(context);
            }
        }
    }
}
