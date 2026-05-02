package net.fabricmc.fabric.api.networking.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class ServerPlayConnectionEvents {
    public static final SimpleEvent<Disconnect> DISCONNECT = new SimpleEvent<>();

    private ServerPlayConnectionEvents() {
    }

    public static void fireDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        DISCONNECT.fire(callback -> callback.onPlayDisconnect(handler, server));
    }

    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server);
    }
}
