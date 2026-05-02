package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class ServerEntityEvents {
    public static final SimpleEvent<Load> ENTITY_LOAD = new SimpleEvent<>();
    public static final SimpleEvent<Unload> ENTITY_UNLOAD = new SimpleEvent<>();

    private ServerEntityEvents() {
    }

    public static void fireEntityLoad(Entity entity, ServerLevel level) {
        ENTITY_LOAD.fire(callback -> callback.onLoad(entity, level));
    }

    public static void fireEntityUnload(Entity entity, ServerLevel level) {
        ENTITY_UNLOAD.fire(callback -> callback.onUnload(entity, level));
    }

    @FunctionalInterface
    public interface Load {
        void onLoad(Entity entity, ServerLevel level);
    }

    @FunctionalInterface
    public interface Unload {
        void onUnload(Entity entity, ServerLevel level);
    }
}
