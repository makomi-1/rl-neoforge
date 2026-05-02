package net.fabricmc.fabric.api.networking.v1;

import net.fabricmc.fabric.impl.networking.NetworkCompatRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerPlayNetworking {
    private ServerPlayNetworking() {
    }

    public static <T extends CustomPacketPayload> void registerGlobalReceiver(
        CustomPacketPayload.Type<T> type,
        PlayPayloadHandler<T> handler
    ) {
        NetworkCompatRegistry.registerServerHandler(type, handler);
    }

    public static void send(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void send(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        PacketDistributor.sendToPlayer(player, payload, payloads);
    }

    @FunctionalInterface
    public interface PlayPayloadHandler<T extends CustomPacketPayload> {
        void receive(T payload, Context context);
    }

    public static final class Context {
        private final IPayloadContext delegate;

        public Context(IPayloadContext delegate) {
            this.delegate = delegate;
        }

        public ServerPlayer player() {
            try {
                Player player = delegate.player();
                return player instanceof ServerPlayer serverPlayer ? serverPlayer : null;
            } catch (UnsupportedOperationException ex) {
                return null;
            }
        }
    }
}
