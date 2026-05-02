package com.makomi;

import com.makomi.client.ClientHooks;
import com.makomi.client.bench.BenchClientAutomationController;
import com.makomi.client.bench.BenchClientCommandBridge;
import com.makomi.client.config.RedstoneLinkClientDisplayConfig;
import com.makomi.client.network.BenchCommandNetworkClientHandlerSupport;
import com.makomi.client.network.ChunkActivatorNetworkClientHandlerSupport;
import com.makomi.client.network.LinkFilterNetworkClientHandlerSupport;
import com.makomi.client.network.PairingNetworkClientHandlerSupport;
import com.makomi.client.network.QuickLinkNetworkClientHandlerSupport;
import com.makomi.client.network.RepeaterNetworkClientHandlerSupport;
import com.makomi.client.network.StatePanelNetworkClientHandlerSupport;
import com.makomi.client.render.ChunkActivatorFarOverlayRenderer;
import com.makomi.client.render.DirectionalFaceVectorWorldOverlayRenderer;
import com.makomi.client.render.HideChunkActivatorGhostRenderer;
import com.makomi.client.render.HideFilterGhostRenderer;
import com.makomi.client.render.HideNodeGhostRenderer;
import com.makomi.client.render.LinkFilterAreaRenderer;
import com.makomi.client.render.LinkNodeFarOverlayRenderer;
import com.makomi.client.render.LinkSerialHudOverlayRenderer;
import com.makomi.client.render.QuickLinkWorldOverlayRenderer;
import com.makomi.client.screen.SmartNodeContainerScreen;
import com.makomi.client.screen.TriggerSourcePairingScreen;
import com.makomi.client.web.LocalWebAppBridgeService;
import com.makomi.data.LinkItemData;
import com.makomi.data.NodeAliasDisplayUtil;
import com.makomi.data.QuickLinkToolData;
import com.makomi.data.SmartGlassesAccessSupport;
import com.makomi.data.SmartNodeContainerData;
import com.makomi.data.SmartNodeContainerPlacementType;
import com.makomi.item.DirectionalFaceEditorItem;
import com.makomi.item.QuickLinkToolItem;
import com.makomi.item.SyncLinkerItem;
import com.makomi.network.DirectionalFaceEditorNetwork;
import com.makomi.network.PairingNetwork;
import com.makomi.network.QuickLinkNetwork;
import com.makomi.network.SmartNodeContainerNetwork;
import com.makomi.network.StatePanelNetwork;
import com.makomi.registry.ModBlockEntities;
import com.makomi.registry.ModBlocks;
import com.makomi.registry.ModItems;
import com.makomi.registry.ModMenuTypes;
import com.makomi.util.SignalStrengths;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import java.net.URI;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.MenuScreens;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWScrollCallbackI;

public class RedstoneLinkClient implements ClientModInitializer {
    private static final String KEY_CATEGORY = "key.categories.redstonelink";
    private static final String KEY_TOGGLE_SERIAL_OVERLAY = "key.redstonelink.toggle_serial_overlay";
    private static final String KEY_TOGGLE_SMART_GLASSES_FACE_VECTORS =
        "key.redstonelink.toggle_smart_glasses_face_vectors";
    private static final String KEY_TOGGLE_QUICK_LINK_MODE = "key.redstonelink.toggle_quick_link_mode";
    private static final String CLIENT_DISPLAY_COMMAND_ROOT = "rlclient";
    private static KeyMapping toggleSerialOverlayKey;
    private static KeyMapping toggleSmartGlassesFaceVectorsKey;
    private static KeyMapping toggleQuickLinkModeKey;
    private static boolean pickItemKeyWasDown;
    private static long syncLinkerScrollHookWindowHandle;
    private static GLFWScrollCallback syncLinkerScrollCallback;
    private static GLFWScrollCallbackI previousSyncLinkerScrollCallback;

    @Override
    public void onInitializeClient() {
        RedstoneLinkClientDisplayConfig.load();
        registerRenderLayers();
        registerBlockEntityRenderers();
        registerMenuScreens();
        registerHudRenderers();
        registerClientKeyBindings();
        registerClientCommands();
        registerClientLifecycleHooks();
        registerPairingScreenOpeners();
        registerPairingPacketReceivers();
        registerBenchCommandClientHooks();
        registerQuickLinkClientHooks();
        registerDirectionalFaceVectorClientHooks();
        registerStatePanelClientHooks();
        registerLinkFilterClientHooks();
        registerChunkActivatorClientHooks();
        registerRepeaterClientHooks();
        BenchClientAutomationController.initialize();
        RedstoneLink.LOGGER.info("RedstoneLink client initialized");
    }

    private static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_REDSTONE_CORE, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_REDSTONE_CORE_TRANSPARENT, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_REDSTONE_DUST_CORE_TRANSPARENT, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_TOGGLE_BUTTON, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_PUSH_BUTTON, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_SYNC_LEVER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_TOGGLE_EMITTER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_PULSE_EMITTER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_SYNC_EMITTER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_SEND_FILTER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_RECEIVE_FILTER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_CHUNK_ACTIVATOR, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_REPEATER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LINK_REDSTONE_DUST_CORE, RenderType.translucent());
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.LINK_REDSTONE_CORE, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_REDSTONE_CORE_TRANSPARENT, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_CORE, HideNodeGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_REDSTONE_DUST_CORE, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_REDSTONE_DUST_CORE_TRANSPARENT, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_TOGGLE_BUTTON, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_PUSH_BUTTON, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_SYNC_LEVER, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_TOGGLE_EMITTER, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_TOGGLE_EMITTER, HideNodeGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_PULSE_EMITTER, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_PULSE_EMITTER, HideNodeGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_SYNC_EMITTER, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_SYNC_TRIGGER_SOURCE, HideNodeGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_SEND_FILTER, LinkFilterAreaRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_SEND_FILTER, HideFilterGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_RECEIVE_FILTER, LinkFilterAreaRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_RECEIVE_FILTER, HideFilterGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_CHUNK_ACTIVATOR, ChunkActivatorFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_CHUNK_ACTIVATOR, HideChunkActivatorGhostRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LINK_REPEATER, LinkNodeFarOverlayRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HIDE_REPEATER, HideNodeGhostRenderer::new);
    }

    private static void registerHudRenderers() {
        HudRenderCallback.EVENT.register(LinkSerialHudOverlayRenderer::onHudRender);
    }

    private static void registerMenuScreens() {
        MenuScreens.register(ModMenuTypes.SMART_NODE_CONTAINER, SmartNodeContainerScreen::new);
    }

    private static void registerClientKeyBindings() {
        InputConstants.Key defaultToggleKey = RedstoneLinkClientDisplayConfig.overlay().toggleKey();
        InputConstants.Key defaultSmartGlassesFaceVectorToggleKey =
            RedstoneLinkClientDisplayConfig.overlay().faceVectorToggleKey();
        InputConstants.Key defaultQuickLinkToggleKey = RedstoneLinkClientDisplayConfig.quickLink().modeToggleKey();
        toggleSerialOverlayKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(KEY_TOGGLE_SERIAL_OVERLAY, defaultToggleKey.getType(), defaultToggleKey.getValue(), KEY_CATEGORY)
        );
        toggleSmartGlassesFaceVectorsKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                KEY_TOGGLE_SMART_GLASSES_FACE_VECTORS,
                defaultSmartGlassesFaceVectorToggleKey.getType(),
                defaultSmartGlassesFaceVectorToggleKey.getValue(),
                KEY_CATEGORY
            )
        );
        toggleQuickLinkModeKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(KEY_TOGGLE_QUICK_LINK_MODE, defaultQuickLinkToggleKey.getType(), defaultQuickLinkToggleKey.getValue(), KEY_CATEGORY)
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ensureSyncLinkerScrollHookInstalled(client);
            while (toggleSerialOverlayKey.consumeClick()) {
                if (Screen.hasControlDown()) {
                    continue;
                }
                RedstoneLinkClientDisplayConfig.SerialOverlayMode mode = RedstoneLinkClientDisplayConfig.cycleSerialOverlayMode();
                if (client.player != null) {
                    client.player.displayClientMessage(Component.translatable(mode.messageKey()), true);
                }
            }

            while (toggleSmartGlassesFaceVectorsKey.consumeClick()) {
                if (!Screen.hasControlDown()) {
                    continue;
                }
                handleSmartGlassesFaceVectorToggle(client);
            }

            while (toggleQuickLinkModeKey.consumeClick()) {
                handleQuickLinkModeKeyPress(client);
            }

            boolean pickItemKeyDown = client.options.keyPickItem.isDown();
            if (pickItemKeyDown && !pickItemKeyWasDown) {
                handlePickItemShortcut(client);
            }
            pickItemKeyWasDown = pickItemKeyDown;
        });
    }

    private static void handleSmartGlassesFaceVectorToggle(Minecraft client) {
        if (client == null || client.player == null) {
            return;
        }
        boolean enabled = RedstoneLinkClientDisplayConfig.toggleSmartGlassesFaceVectorEnabled();
        client.player.displayClientMessage(
            Component.translatable(
                enabled
                    ? "message.redstonelink.smart_glasses.face_vectors.enabled"
                    : "message.redstonelink.smart_glasses.face_vectors.disabled"
            ),
            true
        );
    }

    private static void handleQuickLinkModeKeyPress(Minecraft client) {
        if (client == null || client.player == null) {
            return;
        }
        if (isHoldingSmartNodeContainer(client.player)) {
            handleSmartNodeContainerOpen(client);
            return;
        }
        if (client.player.isShiftKeyDown()) {
            handleQuickLinkClear(client);
            return;
        }
        handleQuickLinkModeToggle(client);
    }

    private static void handleQuickLinkModeToggle(Minecraft client) {
        if (client == null || client.player == null || client.screen != null) {
            return;
        }
        if (!(client.player.getMainHandItem().getItem() instanceof QuickLinkToolItem)) {
            return;
        }

        QuickLinkToolData.Snapshot snapshot = QuickLinkToolData.read(client.player.getMainHandItem());
        QuickLinkToolData.Mode nextMode = snapshot.mode().next();
        ClientPlayNetworking.send(
            new QuickLinkNetwork.SaveQuickLinkPayload(
                nextMode.token(),
                com.makomi.data.LinkNodeSemantics.toSemanticName(snapshot.serialCacheType()),
                snapshot.serialCacheExpression(),
                snapshot.channelCache(),
                snapshot.applyEditMode().token()
            )
        );
        client.player.displayClientMessage(
            Component.translatable(
                "message.redstonelink.quick_link.mode_switched",
                Component.translatable(nextMode.translationKey())
            ),
            true
        );
    }

    private static void handleQuickLinkClear(Minecraft client) {
        if (client == null || client.player == null || client.screen != null) {
            return;
        }
        if (SmartGlassesAccessSupport.canOperateQuickLinkVisualization(client.player)) {
            QuickLinkNetworkClientHandlerSupport.clearVisualizedObjects();
            return;
        }
        if (!(client.player.getMainHandItem().getItem() instanceof QuickLinkToolItem)) {
            return;
        }

        QuickLinkToolData.Snapshot cleared = QuickLinkToolData.clearCaches(client.player.getMainHandItem());
        ClientPlayNetworking.send(
            new QuickLinkNetwork.SaveQuickLinkPayload(
                cleared.mode().token(),
                com.makomi.data.LinkNodeSemantics.toSemanticName(cleared.serialCacheType()),
                cleared.serialCacheExpression(),
                cleared.channelCache(),
                cleared.applyEditMode().token()
            )
        );
        client.player.displayClientMessage(Component.translatable("message.redstonelink.quick_link.cache_cleared"), true);
    }

    private static void handlePickItemShortcut(Minecraft client) {
        if (client == null || client.player == null || client.screen != null) {
            return;
        }
        if (isHoldingSmartNodeContainer(client.player)) {
            handleSmartNodeContainerTypeCycle(client);
            return;
        }
        if (client.player.getMainHandItem().getItem() instanceof QuickLinkToolItem) {
            handleQuickLinkApplyEditModeToggle(client);
            return;
        }
        if (!(client.player.getMainHandItem().getItem() instanceof DirectionalFaceEditorItem)) {
            return;
        }
        handleDirectionalFaceEditorModeCycle(client);
    }

    private static void handleQuickLinkApplyEditModeToggle(Minecraft client) {
        if (client == null || client.player == null || QuickLinkNetworkClientHandlerSupport.isVisualizeMode(client)) {
            return;
        }
        QuickLinkToolData.Snapshot nextSnapshot = QuickLinkToolData.cycleApplyEditMode(client.player.getMainHandItem());
        ClientPlayNetworking.send(
            new QuickLinkNetwork.SaveQuickLinkPayload(
                nextSnapshot.mode().token(),
                com.makomi.data.LinkNodeSemantics.toSemanticName(nextSnapshot.serialCacheType()),
                nextSnapshot.serialCacheExpression(),
                nextSnapshot.channelCache(),
                nextSnapshot.applyEditMode().token()
            )
        );
        client.player.displayClientMessage(
            Component.translatable(
                "message.redstonelink.quick_link.apply_edit_mode_switched",
                Component.translatable(nextSnapshot.applyEditMode().translationKey())
            ),
            true
        );
    }

    private static void ensureSyncLinkerScrollHookInstalled(Minecraft client) {
        if (client == null || client.getWindow() == null) {
            return;
        }
        long windowHandle = client.getWindow().getWindow();
        if (windowHandle == 0L || windowHandle == syncLinkerScrollHookWindowHandle) {
            return;
        }
        if (syncLinkerScrollCallback == null) {
            syncLinkerScrollCallback = GLFWScrollCallback.create((callbackWindow, horizontalAmount, verticalAmount) -> {
                if (
                    !handleSyncLinkerMouseScroll(Minecraft.getInstance(), horizontalAmount, verticalAmount)
                        && previousSyncLinkerScrollCallback != null
                ) {
                    previousSyncLinkerScrollCallback.invoke(callbackWindow, horizontalAmount, verticalAmount);
                }
            });
        }
        previousSyncLinkerScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, syncLinkerScrollCallback);
        syncLinkerScrollHookWindowHandle = windowHandle;
    }

    private static boolean handleSyncLinkerMouseScroll(Minecraft client, double horizontalAmount, double verticalAmount) {
        if (client == null || client.player == null || client.screen != null || !Screen.hasControlDown()) {
            return false;
        }
        if (verticalAmount == 0.0D) {
            return false;
        }
        if (isHoldingSmartNodeContainer(client.player)) {
            return handleSmartNodeContainerMouseScroll(client, verticalAmount);
        }
        ItemStack mainHandItem = client.player.getMainHandItem();
        if (mainHandItem.isEmpty() || !(mainHandItem.getItem() instanceof SyncLinkerItem)) {
            return false;
        }
        int delta = verticalAmount > 0.0D ? 1 : -1;
        int currentSignalStrength = LinkItemData.getSyncLinkerSignalStrength(mainHandItem);
        int nextSignalStrength = SignalStrengths.clamp(currentSignalStrength + delta);
        LinkItemData.setSyncLinkerSignalStrength(mainHandItem, nextSignalStrength);
        if (client.getConnection() != null) {
            ClientPlayNetworking.send(
                new PairingNetwork.SaveSyncLinkerSignalStrengthPayload(LinkItemData.getSerial(mainHandItem), nextSignalStrength)
            );
        }
        client.player.displayClientMessage(
            Component.translatable("message.redstonelink.sync_linker.signal_strength_changed", Integer.toString(nextSignalStrength)),
            true
        );
        return true;
    }

    private static boolean handleSmartNodeContainerMouseScroll(Minecraft client, double verticalAmount) {
        if (client == null || client.player == null) {
            return false;
        }
        ItemStack mainHandItem = client.player.getMainHandItem();
        if (mainHandItem.isEmpty() || mainHandItem.getItem() != ModItems.SMART_NODE_CONTAINER) {
            return false;
        }
        SmartNodeContainerData.Snapshot snapshot = SmartNodeContainerData.read(mainHandItem);
        if (!snapshot.hasItems()) {
            return false;
        }
        List<Integer> candidateSlotIndexes = SmartNodeContainerData.findSlotsForType(
            SmartNodeContainerData.readContents(mainHandItem, client.player.registryAccess()),
            snapshot.selectedType()
        );
        if (candidateSlotIndexes.isEmpty()) {
            return false;
        }
        int delta = verticalAmount > 0.0D ? 1 : -1;
        int selectedSlotIndex = SmartNodeContainerData.cycleTemporarySelectedSlot(
            SmartNodeContainerData.readContents(mainHandItem, client.player.registryAccess()),
            snapshot.selectedType(),
            snapshot.temporarySelectedSlotIndex(),
            delta
        );
        if (selectedSlotIndex < 0) {
            return false;
        }
        SmartNodeContainerData.writeTemporarySelectedSlot(mainHandItem, selectedSlotIndex);
        ClientPlayNetworking.send(new SmartNodeContainerNetwork.SelectSmartNodeContainerSlotPayload(selectedSlotIndex));

        ItemStack selectedStack = SmartNodeContainerData.readContents(mainHandItem, client.player.registryAccess()).get(selectedSlotIndex);
        client.player.displayClientMessage(
            Component.translatable(
                "message.redstonelink.smart_node_container.temporary_selected",
                NodeAliasDisplayUtil.formatDisplayText(LinkItemData.getDisplayAlias(selectedStack), LinkItemData.getSerial(selectedStack))
            ),
            true
        );
        return true;
    }

    private static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
            ClientCommandManager.literal(CLIENT_DISPLAY_COMMAND_ROOT)
                .then(
                    ClientCommandManager.literal("display")
                        .then(
                            ClientCommandManager.literal("far_overlay")
                                .then(
                                    ClientCommandManager.literal("occluded")
                                        .executes(context -> executeSetFarOverlayDisplayMode(new FabricClientCommandSource(context.getSource()), false))
                                )
                                .then(
                                    ClientCommandManager.literal("see_through")
                                        .executes(context -> executeSetFarOverlayDisplayMode(new FabricClientCommandSource(context.getSource()), true))
                                )
                        )
                )
                .then(
                    ClientCommandManager.literal("web")
                        .then(
                            ClientCommandManager.literal("graph")
                                .executes(context -> executeExportGraphSnapshot(new FabricClientCommandSource(context.getSource())))
                                .then(
                                    ClientCommandManager.literal("open")
                                        .executes(context -> executeOpenGraphPage(new FabricClientCommandSource(context.getSource())))
                                )
                                .then(
                                    ClientCommandManager.literal("export")
                                        .executes(context -> executeExportGraphSnapshot(new FabricClientCommandSource(context.getSource())))
                                )
                        )
                )
        ));
    }

    private static void registerClientLifecycleHooks() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            LocalWebAppBridgeService.stop();
            syncLinkerScrollCallback = null;
            previousSyncLinkerScrollCallback = null;
            syncLinkerScrollHookWindowHandle = 0L;
        });
    }

    private static int executeSetFarOverlayDisplayMode(FabricClientCommandSource source, boolean seeThrough) {
        RedstoneLinkClientDisplayConfig.setFarOverlaySeeThroughEnabled(seeThrough);
        Component modeLabel = Component.translatable(
            seeThrough
                ? "message.redstonelink.display.far_overlay.mode.see_through"
                : "message.redstonelink.display.far_overlay.mode.occluded"
        );
        source.sendFeedback(Component.translatable("message.redstonelink.display.far_overlay.updated", modeLabel));
        return Command.SINGLE_SUCCESS;
    }

    private static int executeOpenGraphPage(FabricClientCommandSource source) {
        try {
            URI graphPageUri = LocalWebAppBridgeService.openGraphPage();
            source.sendFeedback(Component.translatable("message.redstonelink.web.opened", graphPageUri.toString()));
            return Command.SINGLE_SUCCESS;
        } catch (RuntimeException exception) {
            String reason = exception.getMessage() == null || exception.getMessage().isBlank()
                ? exception.getClass().getSimpleName()
                : exception.getMessage();
            RedstoneLink.LOGGER.warn("Failed to open local graph page", exception);
            source.sendFeedback(Component.translatable("message.redstonelink.web.open_failed", reason));
            return 0;
        }
    }

    private static int executeExportGraphSnapshot(FabricClientCommandSource source) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            source.sendFeedback(Component.translatable("message.redstonelink.graph.export.no_connection"));
            return 0;
        }
        ClientPlayNetworking.send(new StatePanelNetwork.ExportStatePanelGraphPayload(false));
        source.sendFeedback(Component.translatable("message.redstonelink.graph.export.requested"));
        return Command.SINGLE_SUCCESS;
    }

    private static void registerPairingScreenOpeners() {
        ClientHooks.setPairingScreenOpener(hand -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) {
                return;
            }
            minecraft.setScreen(new TriggerSourcePairingScreen(hand));
        });
        ClientHooks.setNodePairingScreenOpener((nodeType, nodeSerial, currentTargetSerial) ->
            PairingNetworkClientHandlerSupport.openPairingScreenBySourceType(nodeType, nodeSerial, List.of())
        );
    }

    private static void registerPairingPacketReceivers() {
        PairingNetworkClientHandlerSupport.registerReceivers();
    }

    private static void registerBenchCommandClientHooks() {
        BenchCommandNetworkClientHandlerSupport.registerReceivers();
        BenchClientCommandBridge.registerMessageHooks();
    }

    private static void registerQuickLinkClientHooks() {
        QuickLinkNetworkClientHandlerSupport.registerReceivers();
        QuickLinkNetworkClientHandlerSupport.registerInteractionCallbacks();
        QuickLinkWorldOverlayRenderer.register();
    }

    private static void registerDirectionalFaceVectorClientHooks() {
        DirectionalFaceVectorWorldOverlayRenderer.register();
    }

    private static void registerStatePanelClientHooks() {
        StatePanelNetworkClientHandlerSupport.registerReceivers();
    }

    private static void registerLinkFilterClientHooks() {
        LinkFilterNetworkClientHandlerSupport.registerReceivers();
    }

    private static void registerChunkActivatorClientHooks() {
        ChunkActivatorNetworkClientHandlerSupport.registerReceivers();
    }

    private static void registerRepeaterClientHooks() {
        RepeaterNetworkClientHandlerSupport.registerReceivers();
    }

    private static boolean isHoldingSmartNodeContainer(net.minecraft.world.entity.player.Player player) {
        return player != null && player.getMainHandItem().getItem() == ModItems.SMART_NODE_CONTAINER;
    }

    private static void handleSmartNodeContainerOpen(Minecraft client) {
        if (client == null || client.player == null || client.screen != null) {
            return;
        }
        ClientPlayNetworking.send(new SmartNodeContainerNetwork.OpenSmartNodeContainerPayload());
    }

    private static void handleSmartNodeContainerTypeCycle(Minecraft client) {
        if (client == null || client.player == null) {
            return;
        }
        SmartNodeContainerData.Snapshot snapshot = SmartNodeContainerData.cycleSelectedType(client.player.getMainHandItem());
        SmartNodeContainerPlacementType selectedType = snapshot.selectedType();
        ClientPlayNetworking.send(new SmartNodeContainerNetwork.CycleSmartNodeContainerTypePayload());
        client.player.displayClientMessage(
            Component.translatable(
                "message.redstonelink.smart_node_container.selected_type_switched",
                Component.translatable(selectedType.translationKey())
            ),
            true
        );
    }

    private static void handleDirectionalFaceEditorModeCycle(Minecraft client) {
        if (client == null || client.player == null) {
            return;
        }
        ClientPlayNetworking.send(new DirectionalFaceEditorNetwork.CycleDirectionalFaceEditorModePayload());
    }
}
