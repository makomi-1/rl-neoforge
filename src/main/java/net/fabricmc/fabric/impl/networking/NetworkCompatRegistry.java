package net.fabricmc.fabric.impl.networking;

import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkCompatRegistry {
    private static final String PROTOCOL_VERSION = "1";
    private static final Map<CustomPacketPayload.Type<?>, PayloadRegistration<?>> PLAY_C2S = new LinkedHashMap<>();
    private static final Map<CustomPacketPayload.Type<?>, PayloadRegistration<?>> PLAY_S2C = new LinkedHashMap<>();
    private static final Map<CustomPacketPayload.Type<?>, ClientPlayNetworking.PlayPayloadHandler<?>> CLIENT_HANDLERS =
        new LinkedHashMap<>();
    private static final Map<CustomPacketPayload.Type<?>, ServerPlayNetworking.PlayPayloadHandler<?>> SERVER_HANDLERS =
        new LinkedHashMap<>();

    private NetworkCompatRegistry() {
    }

    public static synchronized <T extends CustomPacketPayload> void registerPlayC2S(
        CustomPacketPayload.Type<T> type,
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec
    ) {
        PLAY_C2S.put(type, new PayloadRegistration<>(type, codec));
    }

    public static synchronized <T extends CustomPacketPayload> void registerPlayS2C(
        CustomPacketPayload.Type<T> type,
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec
    ) {
        PLAY_S2C.put(type, new PayloadRegistration<>(type, codec));
    }

    public static synchronized <T extends CustomPacketPayload> void registerServerHandler(
        CustomPacketPayload.Type<T> type,
        ServerPlayNetworking.PlayPayloadHandler<T> handler
    ) {
        SERVER_HANDLERS.put(type, handler);
    }

    public static synchronized <T extends CustomPacketPayload> void registerClientHandler(
        CustomPacketPayload.Type<T> type,
        ClientPlayNetworking.PlayPayloadHandler<T> handler
    ) {
        CLIENT_HANDLERS.put(type, handler);
    }

    public static synchronized void bootstrap(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        for (PayloadRegistration<?> registration : PLAY_S2C.values()) {
            registerPlayToClient(registrar, registration);
        }
        for (PayloadRegistration<?> registration : PLAY_C2S.values()) {
            registerPlayToServer(registrar, registration);
        }
    }

    private static <T extends CustomPacketPayload> void registerPlayToClient(
        PayloadRegistrar registrar,
        PayloadRegistration<T> registration
    ) {
        registrar.playToClient(
            registration.type(),
            registration.codec(),
            (payload, context) -> dispatchClient(registration.type(), payload)
        );
    }

    private static <T extends CustomPacketPayload> void registerPlayToServer(
        PayloadRegistrar registrar,
        PayloadRegistration<T> registration
    ) {
        registrar.playToServer(
            registration.type(),
            registration.codec(),
            (payload, context) -> dispatchServer(registration.type(), payload, context)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void dispatchServer(
        CustomPacketPayload.Type<T> type,
        T payload,
        IPayloadContext context
    ) {
        ServerPlayNetworking.PlayPayloadHandler<T> handler =
            (ServerPlayNetworking.PlayPayloadHandler<T>) SERVER_HANDLERS.get(type);
        if (handler != null) {
            handler.receive(payload, new ServerPlayNetworking.Context(context));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void dispatchClient(
        CustomPacketPayload.Type<T> type,
        T payload
    ) {
        ClientPlayNetworking.PlayPayloadHandler<T> handler =
            (ClientPlayNetworking.PlayPayloadHandler<T>) CLIENT_HANDLERS.get(type);
        if (handler != null) {
            handler.receive(payload, ClientPlayNetworking.Context.create());
        }
    }

    private record PayloadRegistration<T extends CustomPacketPayload>(
        CustomPacketPayload.Type<T> type,
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec
    ) {
    }
}
