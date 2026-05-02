package net.fabricmc.fabric.api.client.rendering.v1;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.fabricmc.fabric.impl.client.NeoForgeClientRegistries;

@FunctionalInterface
public interface HudRenderCallback {
    void onHudRender(GuiGraphics guiGraphics, DeltaTracker tickCounter);

    Event EVENT = new Event();

    final class Event {
        private final SimpleEvent<HudRenderCallback> callbacks = new SimpleEvent<>();

        public void register(HudRenderCallback callback) {
            callbacks.register(callback);
            NeoForgeClientRegistries.registerHudRenderer((guiGraphics, deltaTracker) ->
                callback.onHudRender(guiGraphics, deltaTracker)
            );
        }
    }
}
