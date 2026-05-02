package com.makomi.registry;

import com.makomi.RedstoneLink;
import com.makomi.block.entity.HideChunkActivatorBlockEntity;
import com.makomi.block.entity.HideCoreBlockEntity;
import com.makomi.block.entity.HidePulseEmitterBlockEntity;
import com.makomi.block.entity.HideReceiveFilterBlockEntity;
import com.makomi.block.entity.HideRepeaterBlockEntity;
import com.makomi.block.entity.HideSendFilterBlockEntity;
import com.makomi.block.entity.HideSyncTriggerSourceBlockEntity;
import com.makomi.block.entity.HideToggleEmitterBlockEntity;
import com.makomi.block.entity.LinkChunkActivatorBlockEntity;
import com.makomi.block.entity.LinkCoreBlockEntity;
import com.makomi.block.entity.LinkPulseButtonBlockEntity;
import com.makomi.block.entity.LinkPulseEmitterBlockEntity;
import com.makomi.block.entity.LinkReceiveFilterBlockEntity;
import com.makomi.block.entity.LinkRedstoneDustCoreBlockEntity;
import com.makomi.block.entity.LinkRepeaterBlockEntity;
import com.makomi.block.entity.LinkSendFilterBlockEntity;
import com.makomi.block.entity.LinkSyncEmitterBlockEntity;
import com.makomi.block.entity.LinkSyncLeverBlockEntity;
import com.makomi.block.entity.LinkToggleButtonBlockEntity;
import com.makomi.block.entity.LinkToggleEmitterBlockEntity;
import com.makomi.block.entity.LinkTransparentCoreBlockEntity;
import com.makomi.block.entity.LinkTransparentRedstoneDustCoreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ModBlockEntities {
    public static BlockEntityType<LinkCoreBlockEntity> LINK_REDSTONE_CORE;
    public static BlockEntityType<LinkTransparentCoreBlockEntity> LINK_REDSTONE_CORE_TRANSPARENT;
    public static BlockEntityType<HideCoreBlockEntity> HIDE_CORE;
    public static BlockEntityType<LinkToggleButtonBlockEntity> LINK_TOGGLE_BUTTON;
    public static BlockEntityType<LinkSyncLeverBlockEntity> LINK_SYNC_LEVER;
    public static BlockEntityType<LinkPulseButtonBlockEntity> LINK_PUSH_BUTTON;
    public static BlockEntityType<LinkToggleEmitterBlockEntity> LINK_TOGGLE_EMITTER;
    public static BlockEntityType<HideToggleEmitterBlockEntity> HIDE_TOGGLE_EMITTER;
    public static BlockEntityType<LinkPulseEmitterBlockEntity> LINK_PULSE_EMITTER;
    public static BlockEntityType<HidePulseEmitterBlockEntity> HIDE_PULSE_EMITTER;
    public static BlockEntityType<LinkSyncEmitterBlockEntity> LINK_SYNC_EMITTER;
    public static BlockEntityType<HideSyncTriggerSourceBlockEntity> HIDE_SYNC_TRIGGER_SOURCE;
    public static BlockEntityType<LinkSendFilterBlockEntity> LINK_SEND_FILTER;
    public static BlockEntityType<HideSendFilterBlockEntity> HIDE_SEND_FILTER;
    public static BlockEntityType<LinkReceiveFilterBlockEntity> LINK_RECEIVE_FILTER;
    public static BlockEntityType<HideReceiveFilterBlockEntity> HIDE_RECEIVE_FILTER;
    public static BlockEntityType<LinkChunkActivatorBlockEntity> LINK_CHUNK_ACTIVATOR;
    public static BlockEntityType<HideChunkActivatorBlockEntity> HIDE_CHUNK_ACTIVATOR;
    public static BlockEntityType<LinkRepeaterBlockEntity> LINK_REPEATER;
    public static BlockEntityType<HideRepeaterBlockEntity> HIDE_REPEATER;
    public static BlockEntityType<LinkRedstoneDustCoreBlockEntity> LINK_REDSTONE_DUST_CORE;
    public static BlockEntityType<LinkTransparentRedstoneDustCoreBlockEntity> LINK_REDSTONE_DUST_CORE_TRANSPARENT;

    private static boolean registered;

    private ModBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        if (registered) {
            return;
        }
        registered = true;
        modEventBus.addListener(ModBlockEntities::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.BLOCK_ENTITY_TYPE) {
            return;
        }

        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_redstone_core"), () ->
            LINK_REDSTONE_CORE = BlockEntityType.Builder.of(LinkCoreBlockEntity::new, ModBlocks.LINK_REDSTONE_CORE).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_redstone_core_transparent"), () ->
            LINK_REDSTONE_CORE_TRANSPARENT = BlockEntityType.Builder.of(
                LinkTransparentCoreBlockEntity::new,
                ModBlocks.LINK_REDSTONE_CORE_TRANSPARENT
            ).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_core"), () ->
            HIDE_CORE = BlockEntityType.Builder.of(HideCoreBlockEntity::new, ModBlocks.HIDE_CORE).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_toggle_button"), () ->
            LINK_TOGGLE_BUTTON = BlockEntityType.Builder.of(LinkToggleButtonBlockEntity::new, ModBlocks.LINK_TOGGLE_BUTTON).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_sync_lever"), () ->
            LINK_SYNC_LEVER = BlockEntityType.Builder.of(LinkSyncLeverBlockEntity::new, ModBlocks.LINK_SYNC_LEVER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_push_button"), () ->
            LINK_PUSH_BUTTON = BlockEntityType.Builder.of(LinkPulseButtonBlockEntity::new, ModBlocks.LINK_PUSH_BUTTON).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_toggle_emitter"), () ->
            LINK_TOGGLE_EMITTER = BlockEntityType.Builder.of(LinkToggleEmitterBlockEntity::new, ModBlocks.LINK_TOGGLE_EMITTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_toggle_emitter"), () ->
            HIDE_TOGGLE_EMITTER = BlockEntityType.Builder.of(HideToggleEmitterBlockEntity::new, ModBlocks.HIDE_TOGGLE_EMITTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_pulse_emitter"), () ->
            LINK_PULSE_EMITTER = BlockEntityType.Builder.of(LinkPulseEmitterBlockEntity::new, ModBlocks.LINK_PULSE_EMITTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_pulse_emitter"), () ->
            HIDE_PULSE_EMITTER = BlockEntityType.Builder.of(HidePulseEmitterBlockEntity::new, ModBlocks.HIDE_PULSE_EMITTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_sync_emitter"), () ->
            LINK_SYNC_EMITTER = BlockEntityType.Builder.of(LinkSyncEmitterBlockEntity::new, ModBlocks.LINK_SYNC_EMITTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_sync_trigger_source"), () ->
            HIDE_SYNC_TRIGGER_SOURCE = BlockEntityType.Builder.of(
                HideSyncTriggerSourceBlockEntity::new,
                ModBlocks.HIDE_SYNC_TRIGGER_SOURCE
            ).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_send_filter"), () ->
            LINK_SEND_FILTER = BlockEntityType.Builder.of(LinkSendFilterBlockEntity::new, ModBlocks.LINK_SEND_FILTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_send_filter"), () ->
            HIDE_SEND_FILTER = BlockEntityType.Builder.of(HideSendFilterBlockEntity::new, ModBlocks.HIDE_SEND_FILTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_receive_filter"), () ->
            LINK_RECEIVE_FILTER = BlockEntityType.Builder.of(LinkReceiveFilterBlockEntity::new, ModBlocks.LINK_RECEIVE_FILTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_receive_filter"), () ->
            HIDE_RECEIVE_FILTER = BlockEntityType.Builder.of(HideReceiveFilterBlockEntity::new, ModBlocks.HIDE_RECEIVE_FILTER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_chunk_activator"), () ->
            LINK_CHUNK_ACTIVATOR = BlockEntityType.Builder.of(LinkChunkActivatorBlockEntity::new, ModBlocks.LINK_CHUNK_ACTIVATOR).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_chunk_activator"), () ->
            HIDE_CHUNK_ACTIVATOR = BlockEntityType.Builder.of(
                HideChunkActivatorBlockEntity::new,
                ModBlocks.HIDE_CHUNK_ACTIVATOR
            ).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_repeater"), () ->
            LINK_REPEATER = BlockEntityType.Builder.of(LinkRepeaterBlockEntity::new, ModBlocks.LINK_REPEATER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("hide_repeater"), () ->
            HIDE_REPEATER = BlockEntityType.Builder.of(HideRepeaterBlockEntity::new, ModBlocks.HIDE_REPEATER).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_redstone_dust_core"), () ->
            LINK_REDSTONE_DUST_CORE = BlockEntityType.Builder.of(
                LinkRedstoneDustCoreBlockEntity::new,
                ModBlocks.LINK_REDSTONE_DUST_CORE
            ).build(null)
        );
        event.register(Registries.BLOCK_ENTITY_TYPE, id("link_redstone_dust_core_transparent"), () ->
            LINK_REDSTONE_DUST_CORE_TRANSPARENT = BlockEntityType.Builder.of(
                LinkTransparentRedstoneDustCoreBlockEntity::new,
                ModBlocks.LINK_REDSTONE_DUST_CORE_TRANSPARENT
            ).build(null)
        );
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(RedstoneLink.MOD_ID, path);
    }
}
