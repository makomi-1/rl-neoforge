package net.fabricmc.fabric.api.client.command.v2;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public final class FabricClientCommandSource {
    private final CommandSourceStack delegate;

    public FabricClientCommandSource(CommandSourceStack delegate) {
        this.delegate = delegate;
    }

    public CommandSourceStack stack() {
        return delegate;
    }

    public Minecraft client() {
        return Minecraft.getInstance();
    }

    public void sendFeedback(Component message) {
        if (delegate.getPlayer() != null) {
            delegate.getPlayer().displayClientMessage(message, false);
            return;
        }
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.displayClientMessage(message, false);
        }
    }
}
