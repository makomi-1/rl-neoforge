package net.fabricmc.fabric.api.client.networking.v1;

import java.util.Objects;
import net.fabricmc.fabric.impl.networking.NetworkCompatRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Fabric 客户端网络兼容层。
 */
public final class ClientPlayNetworking {
    private ClientPlayNetworking() {
    }

    public static <T extends CustomPacketPayload> void registerGlobalReceiver(
        CustomPacketPayload.Type<T> type,
        PlayPayloadHandler<T> handler
    ) {
        NetworkCompatRegistry.registerClientHandler(type, handler);
    }

    public static void send(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @FunctionalInterface
    public interface PlayPayloadHandler<T extends CustomPacketPayload> {
        void receive(T payload, Context context);
    }

    /**
     * 兼容 Fabric 的客户端 payload 上下文。
     */
    public static final class Context {
        private static final Context INSTANCE = new Context();

        private Context() {
        }

        public static Context create() {
            return INSTANCE;
        }

        public Minecraft client() {
            return Objects.requireNonNull(Minecraft.getInstance());
        }
    }
}
