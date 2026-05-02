package com.makomi.registry;

import com.makomi.RedstoneLink;
import com.makomi.block.HideChunkActivatorBlock;
import com.makomi.block.HideCoreBlock;
import com.makomi.block.HidePulseEmitterBlock;
import com.makomi.block.HideReceiveFilterBlock;
import com.makomi.block.HideRepeaterBlock;
import com.makomi.block.HideSendFilterBlock;
import com.makomi.block.HideSyncTriggerSourceBlock;
import com.makomi.block.HideToggleEmitterBlock;
import com.makomi.block.LinkChunkActivatorBlock;
import com.makomi.block.LinkCoreBlock;
import com.makomi.block.LinkPulseButtonBlock;
import com.makomi.block.LinkPulseEmitterBlock;
import com.makomi.block.LinkReceiveFilterBlock;
import com.makomi.block.LinkRedstoneDustCoreBlock;
import com.makomi.block.LinkRepeaterBlock;
import com.makomi.block.LinkSendFilterBlock;
import com.makomi.block.LinkSyncEmitterBlock;
import com.makomi.block.LinkSyncLeverBlock;
import com.makomi.block.LinkToggleButtonBlock;
import com.makomi.block.LinkToggleEmitterBlock;
import com.makomi.block.LinkTransparentCoreBlock;
import com.makomi.block.LinkTransparentRedstoneDustCoreBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ModBlocks {
    public static LinkCoreBlock LINK_REDSTONE_CORE;
    public static LinkTransparentCoreBlock LINK_REDSTONE_CORE_TRANSPARENT;
    public static HideCoreBlock HIDE_CORE;
    public static LinkToggleButtonBlock LINK_TOGGLE_BUTTON;
    public static LinkSyncLeverBlock LINK_SYNC_LEVER;
    public static LinkPulseButtonBlock LINK_PUSH_BUTTON;
    public static LinkToggleEmitterBlock LINK_TOGGLE_EMITTER;
    public static HideToggleEmitterBlock HIDE_TOGGLE_EMITTER;
    public static LinkPulseEmitterBlock LINK_PULSE_EMITTER;
    public static HidePulseEmitterBlock HIDE_PULSE_EMITTER;
    public static LinkSyncEmitterBlock LINK_SYNC_EMITTER;
    public static HideSyncTriggerSourceBlock HIDE_SYNC_TRIGGER_SOURCE;
    public static LinkSendFilterBlock LINK_SEND_FILTER;
    public static HideSendFilterBlock HIDE_SEND_FILTER;
    public static LinkReceiveFilterBlock LINK_RECEIVE_FILTER;
    public static HideReceiveFilterBlock HIDE_RECEIVE_FILTER;
    public static LinkChunkActivatorBlock LINK_CHUNK_ACTIVATOR;
    public static HideChunkActivatorBlock HIDE_CHUNK_ACTIVATOR;
    public static LinkRepeaterBlock LINK_REPEATER;
    public static HideRepeaterBlock HIDE_REPEATER;
    public static LinkRedstoneDustCoreBlock LINK_REDSTONE_DUST_CORE;
    public static LinkTransparentRedstoneDustCoreBlock LINK_REDSTONE_DUST_CORE_TRANSPARENT;

    private static boolean registered;

    private ModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        if (registered) {
            return;
        }
        registered = true;
        modEventBus.addListener(ModBlocks::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.BLOCK) {
            return;
        }

        event.register(Registries.BLOCK, id("link_redstone_core"), () ->
            LINK_REDSTONE_CORE = new LinkCoreBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).lightLevel(state -> 0).noOcclusion()
            )
        );
        event.register(Registries.BLOCK, id("link_redstone_core_transparent"), () ->
            LINK_REDSTONE_CORE_TRANSPARENT = new LinkTransparentCoreBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)
                    .lightLevel(state -> 0)
                    .noOcclusion()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isValidSpawn((state, level, pos, entityType) -> false)
            )
        );
        event.register(Registries.BLOCK, id("hide_core"), () ->
            HIDE_CORE = new HideCoreBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)
                    .lightLevel(state -> 0)
                    .noOcclusion()
                    .noCollission()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isValidSpawn((state, level, pos, entityType) -> false)
            )
        );
        event.register(Registries.BLOCK, id("link_toggle_button"), () ->
            LINK_TOGGLE_BUTTON = new LinkToggleButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON))
        );
        event.register(Registries.BLOCK, id("link_sync_lever"), () ->
            LINK_SYNC_LEVER = new LinkSyncLeverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LEVER))
        );
        event.register(Registries.BLOCK, id("link_push_button"), () ->
            LINK_PUSH_BUTTON = new LinkPulseButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON))
        );
        event.register(Registries.BLOCK, id("link_toggle_emitter"), () ->
            LINK_TOGGLE_EMITTER = new LinkToggleEmitterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_toggle_emitter"), () ->
            HIDE_TOGGLE_EMITTER = new HideToggleEmitterBlock(createHideObserverLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_pulse_emitter"), () ->
            LINK_PULSE_EMITTER = new LinkPulseEmitterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_pulse_emitter"), () ->
            HIDE_PULSE_EMITTER = new HidePulseEmitterBlock(createHideObserverLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_sync_emitter"), () ->
            LINK_SYNC_EMITTER = new LinkSyncEmitterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_sync_trigger_source"), () ->
            HIDE_SYNC_TRIGGER_SOURCE = new HideSyncTriggerSourceBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER)
                    .noOcclusion()
                    .noCollission()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isValidSpawn((state, level, pos, entityType) -> false)
            )
        );
        event.register(Registries.BLOCK, id("link_send_filter"), () ->
            LINK_SEND_FILTER = new LinkSendFilterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_send_filter"), () ->
            HIDE_SEND_FILTER = new HideSendFilterBlock(createHideObserverLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_receive_filter"), () ->
            LINK_RECEIVE_FILTER = new LinkReceiveFilterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_receive_filter"), () ->
            HIDE_RECEIVE_FILTER = new HideReceiveFilterBlock(createHideRedstoneBlockLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_chunk_activator"), () ->
            LINK_CHUNK_ACTIVATOR = new LinkChunkActivatorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_chunk_activator"), () ->
            HIDE_CHUNK_ACTIVATOR = new HideChunkActivatorBlock(createHideObserverLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_repeater"), () ->
            LINK_REPEATER = new LinkRepeaterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER).noOcclusion())
        );
        event.register(Registries.BLOCK, id("hide_repeater"), () ->
            HIDE_REPEATER = new HideRepeaterBlock(createHideObserverLikeProperties())
        );
        event.register(Registries.BLOCK, id("link_redstone_dust_core"), () ->
            LINK_REDSTONE_DUST_CORE = new LinkRedstoneDustCoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WIRE))
        );
        event.register(Registries.BLOCK, id("link_redstone_dust_core_transparent"), () ->
            LINK_REDSTONE_DUST_CORE_TRANSPARENT = new LinkTransparentRedstoneDustCoreBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WIRE)
            )
        );
    }

    private static BlockBehaviour.Properties createHideObserverLikeProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OBSERVER)
            .noOcclusion()
            .noCollission()
            .isViewBlocking((state, level, pos) -> false)
            .isSuffocating((state, level, pos) -> false)
            .isRedstoneConductor((state, level, pos) -> false)
            .isValidSpawn((state, level, pos, entityType) -> false);
    }

    private static BlockBehaviour.Properties createHideRedstoneBlockLikeProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)
            .lightLevel(state -> 0)
            .noOcclusion()
            .noCollission()
            .isViewBlocking((state, level, pos) -> false)
            .isSuffocating((state, level, pos) -> false)
            .isRedstoneConductor((state, level, pos) -> false)
            .isValidSpawn((state, level, pos, entityType) -> false);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(RedstoneLink.MOD_ID, path);
    }
}
