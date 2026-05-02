package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.client.Minecraft;

/**
 * Fabric 客户端 tick 回调兼容层。
 */
public final class ClientTickEvents {
    public static final SimpleEvent<EndTick> END_CLIENT_TICK = new SimpleEvent<>();

    private ClientTickEvents() {
    }

    public static void fireEndClientTick(Minecraft client) {
        END_CLIENT_TICK.fire(callback -> callback.onEndTick(client));
    }

    @FunctionalInterface
    public interface EndTick {
        void onEndTick(Minecraft client);
    }
}
