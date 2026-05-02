package net.fabricmc.fabric.api.networking.v1;

import net.fabricmc.fabric.impl.networking.NetworkCompatRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class PayloadTypeRegistry {
    private static final PlayRegistrar PLAY_C2S = new PlayRegistrar(true);
    private static final PlayRegistrar PLAY_S2C = new PlayRegistrar(false);

    private PayloadTypeRegistry() {
    }

    public static PlayRegistrar playC2S() {
        return PLAY_C2S;
    }

    public static PlayRegistrar playS2C() {
        return PLAY_S2C;
    }

    public static void bootstrap(RegisterPayloadHandlersEvent event) {
        NetworkCompatRegistry.bootstrap(event);
    }

    public static final class PlayRegistrar {
        private final boolean serverBound;

        private PlayRegistrar(boolean serverBound) {
            this.serverBound = serverBound;
        }

        public <T extends CustomPacketPayload> void register(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec
        ) {
            if (serverBound) {
                NetworkCompatRegistry.registerPlayC2S(type, codec);
            } else {
                NetworkCompatRegistry.registerPlayS2C(type, codec);
            }
        }
    }
}
