package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {
    public static final SimpleEvent<ServerLifecycle> SERVER_STARTING = new SimpleEvent<>();
    public static final SimpleEvent<ServerLifecycle> SERVER_STARTED = new SimpleEvent<>();
    public static final SimpleEvent<ServerLifecycle> SERVER_STOPPING = new SimpleEvent<>();
    public static final SimpleEvent<ServerLifecycle> SERVER_STOPPED = new SimpleEvent<>();

    private ServerLifecycleEvents() {
    }

    public static void fireServerStarting(MinecraftServer server) {
        SERVER_STARTING.fire(callback -> callback.handle(server));
    }

    public static void fireServerStarted(MinecraftServer server) {
        SERVER_STARTED.fire(callback -> callback.handle(server));
    }

    public static void fireServerStopping(MinecraftServer server) {
        SERVER_STOPPING.fire(callback -> callback.handle(server));
    }

    public static void fireServerStopped(MinecraftServer server) {
        SERVER_STOPPED.fire(callback -> callback.handle(server));
    }

    @FunctionalInterface
    public interface ServerLifecycle {
        void handle(MinecraftServer server);
    }
}
