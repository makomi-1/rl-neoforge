package net.fabricmc.fabric.api.client.screen.v1;

import net.fabricmc.fabric.impl.client.NeoForgeClientRegistries;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class MenuScreens {
    private MenuScreens() {
    }

    public static <M extends AbstractContainerMenu, U extends net.minecraft.client.gui.screens.Screen & MenuAccess<M>> void register(
        MenuType<? extends M> type,
        net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor<M, U> constructor
    ) {
        NeoForgeClientRegistries.registerMenuScreen(type, constructor);
    }
}
