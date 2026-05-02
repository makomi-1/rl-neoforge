package net.fabricmc.fabric.impl.client;

import com.mojang.brigadier.CommandDispatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class NeoForgeClientRegistries {
    private static final List<RenderLayerRegistration> RENDER_LAYERS = new ArrayList<>();
    private static final List<BlockEntityRendererRegistration<?>> BLOCK_ENTITY_RENDERERS = new ArrayList<>();
    private static final List<MenuScreenRegistration<?, ?>> MENU_SCREENS = new ArrayList<>();
    private static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();
    private static final List<ClientCommandRegistration> CLIENT_COMMANDS = new ArrayList<>();
    private static final List<HudRenderRegistration> HUD_RENDERERS = new ArrayList<>();

    private NeoForgeClientRegistries() {
    }

    public static synchronized void registerRenderLayer(Block block, RenderType renderType) {
        RENDER_LAYERS.add(new RenderLayerRegistration(block, renderType));
    }

    public static synchronized <T extends BlockEntity> void registerBlockEntityRenderer(
        BlockEntityType<? extends T> type,
        BlockEntityRendererProvider<T> provider
    ) {
        BLOCK_ENTITY_RENDERERS.add(new BlockEntityRendererRegistration<>(type, provider));
    }

    public static synchronized <M extends AbstractContainerMenu, U extends net.minecraft.client.gui.screens.Screen & net.minecraft.client.gui.screens.inventory.MenuAccess<M>> void registerMenuScreen(
        MenuType<? extends M> type,
        MenuScreens.ScreenConstructor<M, U> constructor
    ) {
        MENU_SCREENS.add(new MenuScreenRegistration<>(type, constructor));
    }

    public static synchronized KeyMapping registerKeyBinding(KeyMapping keyMapping) {
        KEY_MAPPINGS.add(keyMapping);
        return keyMapping;
    }

    public static synchronized void registerClientCommand(ClientCommandRegistration registration) {
        CLIENT_COMMANDS.add(registration);
    }

    public static synchronized void registerHudRenderer(HudRenderRegistration registration) {
        HUD_RENDERERS.add(registration);
    }

    public static synchronized void bootstrapRenderLayers() {
        for (RenderLayerRegistration registration : RENDER_LAYERS) {
            ClientRenderLayerSupport.setRenderLayer(registration.block(), registration.renderType());
        }
    }

    public static synchronized void fireRegisterMenuScreens(RegisterMenuScreensEvent event) {
        for (MenuScreenRegistration<?, ?> registration : MENU_SCREENS) {
            registration.register(event);
        }
    }

    public static synchronized void fireRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        for (KeyMapping keyMapping : KEY_MAPPINGS) {
            event.register(keyMapping);
        }
    }

    public static synchronized void fireRegisterBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (BlockEntityRendererRegistration<?> registration : BLOCK_ENTITY_RENDERERS) {
            registration.register(event);
        }
    }

    public static synchronized void fireRegisterClientCommands(RegisterClientCommandsEvent event) {
        for (ClientCommandRegistration registration : CLIENT_COMMANDS) {
            registration.register(event.getDispatcher(), event.getBuildContext());
        }
    }

    public static synchronized void fireHudRenderers(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        for (HudRenderRegistration registration : HUD_RENDERERS) {
            registration.render(guiGraphics, deltaTracker);
        }
    }

    public interface ClientCommandRegistration {
        void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext);
    }

    @FunctionalInterface
    public interface HudRenderRegistration {
        void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker);
    }

    private record RenderLayerRegistration(Block block, RenderType renderType) {
        private RenderLayerRegistration {
            Objects.requireNonNull(block);
            Objects.requireNonNull(renderType);
        }
    }

    private record BlockEntityRendererRegistration<T extends BlockEntity>(
        BlockEntityType<? extends T> type,
        BlockEntityRendererProvider<T> provider
    ) {
        private void register(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(type, provider);
        }
    }

    private record MenuScreenRegistration<M extends AbstractContainerMenu, U extends net.minecraft.client.gui.screens.Screen & net.minecraft.client.gui.screens.inventory.MenuAccess<M>>(
        MenuType<? extends M> type,
        MenuScreens.ScreenConstructor<M, U> constructor
    ) {
        private void register(RegisterMenuScreensEvent event) {
            event.register(type, constructor);
        }
    }
}
