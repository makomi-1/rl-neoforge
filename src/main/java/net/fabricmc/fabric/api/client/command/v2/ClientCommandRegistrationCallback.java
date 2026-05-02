package net.fabricmc.fabric.api.client.command.v2;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.fabricmc.fabric.impl.client.NeoForgeClientRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface ClientCommandRegistrationCallback {
    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess);

    Event EVENT = new Event();

    final class Event {
        private final SimpleEvent<ClientCommandRegistrationCallback> callbacks = new SimpleEvent<>();

        public void register(ClientCommandRegistrationCallback callback) {
            callbacks.register(callback);
            NeoForgeClientRegistries.registerClientCommand(callback::register);
        }
    }
}
