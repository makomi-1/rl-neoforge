package net.fabricmc.fabric.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * 使用 NeoForge 事件总线驱动 Fabric 客户端兼容事件。
 */
public final class ClientCompatBridge {
    private static boolean registered;

    private ClientCompatBridge() {
    }

    public static synchronized void register() {
        if (registered) {
            return;
        }
        registered = true;
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onClientTickPost);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onClientChatReceived);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onLeftClickBlock);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRenderBlockOutline);
        NeoForge.EVENT_BUS.addListener(ClientCompatBridge::onRenderLevelStage);
    }

    private static void onClientTickPost(ClientTickEvent.Post event) {
        ClientTickEvents.fireEndClientTick(Minecraft.getInstance());
    }

    private static void onClientChatReceived(ClientChatReceivedEvent event) {
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

    public static void dispatchClientPayload(net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        ClientPlayNetworking.PlayPayloadHandler<net.minecraft.network.protocol.common.custom.CustomPacketPayload> handler =
            null;
    }
}
