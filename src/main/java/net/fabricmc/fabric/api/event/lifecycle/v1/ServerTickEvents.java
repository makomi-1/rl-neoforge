package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.server.MinecraftServer;

public final class ServerTickEvents {
    public static final SimpleEvent<StartTick> START_SERVER_TICK = new SimpleEvent<>();
    public static final SimpleEvent<EndTick> END_SERVER_TICK = new SimpleEvent<>();

    private ServerTickEvents() {
    }

    public static void fireStartServerTick(MinecraftServer server) {
        START_SERVER_TICK.fire(callback -> callback.onStartTick(server));
    }

    public static void fireEndServerTick(MinecraftServer server) {
        END_SERVER_TICK.fire(callback -> callback.onEndTick(server));
    }

    @FunctionalInterface
    public interface StartTick {
        void onStartTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface EndTick {
        void onEndTick(MinecraftServer server);
    }
}
