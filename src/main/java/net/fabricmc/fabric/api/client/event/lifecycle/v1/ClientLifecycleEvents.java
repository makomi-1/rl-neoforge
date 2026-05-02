package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.client.Minecraft;

public final class ClientLifecycleEvents {
    public static final StoppingEvent CLIENT_STOPPING = new StoppingEvent();

    private ClientLifecycleEvents() {
    }

    @FunctionalInterface
    public interface ClientStopping {
        void onClientStopping(Minecraft client);
    }

    public static final class StoppingEvent {
        private final SimpleEvent<ClientStopping> callbacks = new SimpleEvent<>();

        public void register(ClientStopping callback) {
            callbacks.register(callback);
        }

        public void fire(Minecraft client) {
            callbacks.fire(callback -> callback.onClientStopping(client));
        }
    }
}
