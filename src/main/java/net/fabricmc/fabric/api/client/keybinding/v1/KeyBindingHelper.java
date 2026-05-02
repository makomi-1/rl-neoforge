package net.fabricmc.fabric.api.client.keybinding.v1;

import net.fabricmc.fabric.impl.client.NeoForgeClientRegistries;
import net.minecraft.client.KeyMapping;

public final class KeyBindingHelper {
    private KeyBindingHelper() {
    }

    public static KeyMapping registerKeyBinding(KeyMapping keyMapping) {
        return NeoForgeClientRegistries.registerKeyBinding(keyMapping);
    }
}
