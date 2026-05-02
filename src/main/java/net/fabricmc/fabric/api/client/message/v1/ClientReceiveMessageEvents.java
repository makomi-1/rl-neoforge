package net.fabricmc.fabric.api.client.message.v1;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.network.chat.Component;

/**
 * Fabric 客户端消息事件兼容层。
 */
public final class ClientReceiveMessageEvents {
    public static final SimpleEvent<Game> GAME = new SimpleEvent<>();

    private ClientReceiveMessageEvents() {
    }

    public static void fireGame(Component message, boolean overlay) {
        GAME.fire(callback -> callback.onReceive(message, overlay));
    }

    @FunctionalInterface
    public interface Game {
        void onReceive(Component message, boolean overlay);
    }
}
