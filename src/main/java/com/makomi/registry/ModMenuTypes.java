package com.makomi.registry;

import com.makomi.RedstoneLink;
import com.makomi.menu.SmartNodeContainerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ModMenuTypes {
    public static MenuType<SmartNodeContainerMenu> SMART_NODE_CONTAINER;

    private static boolean registered;

    private ModMenuTypes() {
    }

    public static void register(IEventBus modEventBus) {
        if (registered) {
            return;
        }
        registered = true;
        modEventBus.addListener(ModMenuTypes::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.MENU) {
            return;
        }
        event.register(Registries.MENU, id("smart_node_container"), () ->
            SMART_NODE_CONTAINER = new MenuType<>(SmartNodeContainerMenu::new, FeatureFlags.DEFAULT_FLAGS)
        );
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(RedstoneLink.MOD_ID, path);
    }
}
