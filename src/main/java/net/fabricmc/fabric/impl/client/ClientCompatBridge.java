package net.fabricmc.fabric.impl.client;

import com.makomi.RedstoneLinkClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class ClientCompatBridge {
    private static boolean registered;
    private static boolean initialized;

    private ClientCompatBridge() {
    }

    public static synchronized void register(IEventBus modEventBus) {
        if (registered) {
            return;
        }
        registered = true;
        modEventBus.addListener(ClientCompatBridge::onRegisterMenuScreens);
        modEventBus.addListener(ClientCompatBridge::onRegisterKeyMappings);
        modEventBus.addListener(ClientCompatBridge::onRegisterBlockEntityRenderers);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRegisterClientCommands);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onClientTickPost);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onClientChatReceived);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onLeftClickBlock);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRenderBlockOutline);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRenderLevelStage);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRenderGuiPost);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onGameShuttingDown);
    }

    private static synchronized void ensureClientInitialized() {
        if (initialized) {
            return;
        }
        initialized = true;
        new RedstoneLinkClient().onInitializeClient();
        NeoForgeClientRegistries.bootstrapRenderLayers();
    }

    private static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        ensureClientInitialized();
        NeoForgeClientRegistries.fireRegisterMenuScreens(event);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ensureClientInitialized();
        NeoForgeClientRegistries.fireRegisterKeyMappings(event);
    }

    private static void onRegisterBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ensureClientInitialized();
        NeoForgeClientRegistries.fireRegisterBlockEntityRenderers(event);
    }

    private static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        ensureClientInitialized();
        NeoForgeClientRegistries.fireRegisterClientCommands(event);
    }

    private static void onClientTickPost(ClientTickEvent.Post event) {
        ensureClientInitialized();
        ClientTickEvents.fireEndClientTick(Minecraft.getInstance());
    }

    private static void onClientChatReceived(ClientChatReceivedEvent event) {
        ensureClientInitialized();
        boolean overlay = event instanceof ClientChatReceivedEvent.System systemEvent && systemEvent.isOverlay();
        ClientReceiveMessageEvents.fireGame(event.getMessage(), overlay);
    }

    private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide || event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) {
            return;
        }
        if (!(event.getEntity() instanceof LocalPlayer player) || event.getFace() == null) {
            return;
        }
        InteractionResult result = AttackBlockCallback.fire(player, event.getLevel(), event.getHand(), event.getPos(), event.getFace());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
        }
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide || !(event.getEntity() instanceof LocalPlayer player)) {
            return;
        }
        InteractionResult result = UseBlockCallback.fire(player, event.getLevel(), event.getHand(), event.getHitVec());
        if (result != InteractionResult.PASS) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private static void onRenderBlockOutline(RenderHighlightEvent.Block event) {
        ensureClientInitialized();
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        WorldRenderContext context = new WorldRenderContext(
            event.getPoseStack(),
            event.getMultiBufferSource(),
            event.getCamera(),
            true
        );
        WorldRenderContext.BlockOutlineContext blockOutlineContext = new WorldRenderContext.BlockOutlineContext(
            event.getTarget().getBlockPos(),
            minecraft.level.getBlockState(event.getTarget().getBlockPos()),
            context.cameraX(),
            context.cameraY(),
            context.cameraZ()
        );
        if (!WorldRenderEvents.fireBlockOutline(context, blockOutlineContext)) {
            event.setCanceled(true);
        }
    }

    private static void onRenderLevelStage(RenderLevelStageEvent event) {
        ensureClientInitialized();
        if (
            event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS
                && event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL
        ) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource consumers = minecraft.renderBuffers().bufferSource();
        PoseStack poseStack = event.getPoseStack();
        WorldRenderContext context = new WorldRenderContext(poseStack, consumers, event.getCamera(), true);
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            WorldRenderEvents.fireAfterTranslucent(context);
        } else {
            WorldRenderEvents.fireLast(context);
        }
        consumers.endBatch();
    }

    private static void onRenderGuiPost(RenderGuiEvent.Post event) {
        ensureClientInitialized();
        NeoForgeClientRegistries.fireHudRenderers(event.getGuiGraphics(), event.getPartialTick());
    }

    private static void onGameShuttingDown(GameShuttingDownEvent event) {
        if (!initialized) {
            return;
        }
        ClientLifecycleEvents.CLIENT_STOPPING.fire(Minecraft.getInstance());
    }
}
