package net.fabricmc.fabric.api.command.v2;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@FunctionalInterface
public interface CommandRegistrationCallback {
    void register(
        CommandDispatcher<CommandSourceStack> dispatcher,
        CommandBuildContext registryAccess,
        Commands.CommandSelection environment
    );

    Event EVENT = new Event();

    final class Event {
        private final SimpleEvent<CommandRegistrationCallback> callbacks = new SimpleEvent<>();

        public void register(CommandRegistrationCallback callback) {
            callbacks.register(callback);
        }

        public void fire(RegisterCommandsEvent event) {
            callbacks.fire(callback -> callback.register(
                event.getDispatcher(),
                event.getBuildContext(),
                event.getCommandSelection()
            ));
        }
    }
}
