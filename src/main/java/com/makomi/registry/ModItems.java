package com.makomi.registry;

import com.makomi.RedstoneLink;
import com.makomi.block.entity.ActivationMode;
import com.makomi.data.LinkFilterKind;
import com.makomi.data.LinkNodeType;
import com.makomi.item.ChunkActivatorBlockItem;
import com.makomi.item.DirectionalFaceEditorItem;
import com.makomi.item.GraphVisualEditorItem;
import com.makomi.item.LinkFilterBlockItem;
import com.makomi.item.LinkerItem;
import com.makomi.item.PairableBlockItem;
import com.makomi.item.QuickLinkToolItem;
import com.makomi.item.RedstoneLinkComponentItem;
import com.makomi.item.RepeaterBlockItem;
import com.makomi.item.SmartGlassesItem;
import com.makomi.item.SmartNodeContainerItem;
import com.makomi.item.StatePanelToolItem;
import com.makomi.item.SyncLinkerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ModItems {
    public static Item RL_ICON;
    public static Item REDSTONE_LINK_COMPONENT;
    public static Item ADV_WIRELESS_AGE_ICON;
    public static Item ADV_COME_FIND_ME_IN_THE_END_ICON;
    public static Item ADV_MASTER_STRATEGIST_ICON;
    public static Item ADV_CONSTELLATION_ICON;
    public static Item LINK_REDSTONE_CORE;
    public static Item LINK_REDSTONE_CORE_TRANSPARENT;
    public static Item HIDE_CORE;
    public static Item LINK_REDSTONE_DUST_CORE;
    public static Item LINK_REDSTONE_DUST_CORE_TRANSPARENT;
    public static Item LINK_TOGGLE_BUTTON;
    public static Item LINK_SYNC_LEVER;
    public static Item LINK_PUSH_BUTTON;
    public static Item LINK_TOGGLE_EMITTER;
    public static Item HIDE_TOGGLE_EMITTER;
    public static Item LINK_PULSE_EMITTER;
    public static Item HIDE_PULSE_EMITTER;
    public static Item LINK_SYNC_EMITTER;
    public static Item HIDE_SYNC_TRIGGER_SOURCE;
    public static Item LINK_SEND_FILTER;
    public static Item HIDE_SEND_FILTER;
    public static Item LINK_RECEIVE_FILTER;
    public static Item HIDE_RECEIVE_FILTER;
    public static Item LINK_CHUNK_ACTIVATOR;
    public static Item HIDE_CHUNK_ACTIVATOR;
    public static Item LINK_REPEATER;
    public static Item HIDE_REPEATER;
    public static Item REDSTONELINK_TOGGLE_LINKER;
    public static Item REDSTONELINK_PULSE_LINKER;
    public static Item REDSTONELINK_SYNC_LINKER;
    public static Item QUICK_LINK_TOOL;
    public static Item SMART_GLASSES;
    public static Item SMART_NODE_CONTAINER;
    public static Item GRAPH_VISUAL_EDITOR;
    public static Item DIRECTIONAL_FACE_EDITOR;
    public static Item REDSTONELINK_STATUS_PANEL;

    private static boolean registered;

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        if (registered) {
            return;
        }
        registered = true;
        modEventBus.addListener(ModItems::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.ITEM) {
            return;
        }

        event.register(Registries.ITEM, id("rl-icon"), () -> RL_ICON = new Item(new Item.Properties()));
        event.register(Registries.ITEM, id("redstone_link_component"), () ->
            REDSTONE_LINK_COMPONENT = new RedstoneLinkComponentItem(new Item.Properties())
        );
        event.register(Registries.ITEM, id("adv_wireless_age_icon"), () -> ADV_WIRELESS_AGE_ICON = new Item(new Item.Properties()));
        event.register(Registries.ITEM, id("adv_come_find_me_in_the_end_icon"), () -> ADV_COME_FIND_ME_IN_THE_END_ICON = new Item(new Item.Properties()));
        event.register(Registries.ITEM, id("adv_master_strategist_icon"), () -> ADV_MASTER_STRATEGIST_ICON = new Item(new Item.Properties()));
        event.register(Registries.ITEM, id("adv_constellation_icon"), () -> ADV_CONSTELLATION_ICON = new Item(new Item.Properties()));
        event.register(Registries.ITEM, id("link_redstone_core"), () ->
            LINK_REDSTONE_CORE = new PairableBlockItem(ModBlocks.LINK_REDSTONE_CORE, new Item.Properties().stacksTo(1), LinkNodeType.CORE)
        );
        event.register(Registries.ITEM, id("link_redstone_core_transparent"), () ->
            LINK_REDSTONE_CORE_TRANSPARENT = new PairableBlockItem(
                ModBlocks.LINK_REDSTONE_CORE_TRANSPARENT,
                new Item.Properties().stacksTo(1),
                LinkNodeType.CORE
            )
        );
        event.register(Registries.ITEM, id("hide_core"), () ->
            HIDE_CORE = new PairableBlockItem(ModBlocks.HIDE_CORE, new Item.Properties().stacksTo(1), LinkNodeType.CORE)
        );
        event.register(Registries.ITEM, id("link_redstone_dust_core"), () ->
            LINK_REDSTONE_DUST_CORE = new PairableBlockItem(ModBlocks.LINK_REDSTONE_DUST_CORE, new Item.Properties().stacksTo(1), LinkNodeType.CORE)
        );
        event.register(Registries.ITEM, id("link_redstone_dust_core_transparent"), () ->
            LINK_REDSTONE_DUST_CORE_TRANSPARENT = new PairableBlockItem(
                ModBlocks.LINK_REDSTONE_DUST_CORE_TRANSPARENT,
                new Item.Properties().stacksTo(1),
                LinkNodeType.CORE
            )
        );
        event.register(Registries.ITEM, id("link_toggle_button"), () ->
            LINK_TOGGLE_BUTTON = new PairableBlockItem(ModBlocks.LINK_TOGGLE_BUTTON, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("link_sync_lever"), () ->
            LINK_SYNC_LEVER = new PairableBlockItem(ModBlocks.LINK_SYNC_LEVER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("link_push_button"), () ->
            LINK_PUSH_BUTTON = new PairableBlockItem(ModBlocks.LINK_PUSH_BUTTON, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("link_toggle_emitter"), () ->
            LINK_TOGGLE_EMITTER = new PairableBlockItem(ModBlocks.LINK_TOGGLE_EMITTER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("hide_toggle_emitter"), () ->
            HIDE_TOGGLE_EMITTER = new PairableBlockItem(ModBlocks.HIDE_TOGGLE_EMITTER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("link_pulse_emitter"), () ->
            LINK_PULSE_EMITTER = new PairableBlockItem(ModBlocks.LINK_PULSE_EMITTER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("hide_pulse_emitter"), () ->
            HIDE_PULSE_EMITTER = new PairableBlockItem(ModBlocks.HIDE_PULSE_EMITTER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("link_sync_emitter"), () ->
            LINK_SYNC_EMITTER = new PairableBlockItem(ModBlocks.LINK_SYNC_EMITTER, new Item.Properties().stacksTo(1), LinkNodeType.TRIGGER_SOURCE)
        );
        event.register(Registries.ITEM, id("hide_sync_trigger_source"), () ->
            HIDE_SYNC_TRIGGER_SOURCE = new PairableBlockItem(
                ModBlocks.HIDE_SYNC_TRIGGER_SOURCE,
                new Item.Properties().stacksTo(1),
                LinkNodeType.TRIGGER_SOURCE
            )
        );
        event.register(Registries.ITEM, id("link_send_filter"), () ->
            LINK_SEND_FILTER = new LinkFilterBlockItem(ModBlocks.LINK_SEND_FILTER, new Item.Properties().stacksTo(1), LinkFilterKind.SEND)
        );
        event.register(Registries.ITEM, id("hide_send_filter"), () ->
            HIDE_SEND_FILTER = new LinkFilterBlockItem(ModBlocks.HIDE_SEND_FILTER, new Item.Properties().stacksTo(1), LinkFilterKind.SEND)
        );
        event.register(Registries.ITEM, id("link_receive_filter"), () ->
            LINK_RECEIVE_FILTER = new LinkFilterBlockItem(ModBlocks.LINK_RECEIVE_FILTER, new Item.Properties().stacksTo(1), LinkFilterKind.RECEIVE)
        );
        event.register(Registries.ITEM, id("hide_receive_filter"), () ->
            HIDE_RECEIVE_FILTER = new LinkFilterBlockItem(ModBlocks.HIDE_RECEIVE_FILTER, new Item.Properties().stacksTo(1), LinkFilterKind.RECEIVE)
        );
        event.register(Registries.ITEM, id("link_chunk_activator"), () ->
            LINK_CHUNK_ACTIVATOR = new ChunkActivatorBlockItem(ModBlocks.LINK_CHUNK_ACTIVATOR, new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("hide_chunk_activator"), () ->
            HIDE_CHUNK_ACTIVATOR = new ChunkActivatorBlockItem(ModBlocks.HIDE_CHUNK_ACTIVATOR, new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("link_repeater"), () ->
            LINK_REPEATER = new RepeaterBlockItem(ModBlocks.LINK_REPEATER, new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("hide_repeater"), () ->
            HIDE_REPEATER = new RepeaterBlockItem(ModBlocks.HIDE_REPEATER, new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("redstonelink_toggle_linker"), () ->
            REDSTONELINK_TOGGLE_LINKER = new LinkerItem(new Item.Properties().stacksTo(1), ActivationMode.TOGGLE)
        );
        event.register(Registries.ITEM, id("redstonelink_pulse_linker"), () ->
            REDSTONELINK_PULSE_LINKER = new LinkerItem(new Item.Properties().stacksTo(1), ActivationMode.PULSE)
        );
        event.register(Registries.ITEM, id("redstonelink_sync_linker"), () ->
            REDSTONELINK_SYNC_LINKER = new SyncLinkerItem(new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("quick_link_tool"), () ->
            QUICK_LINK_TOOL = new QuickLinkToolItem(new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("smart_glasses"), () ->
            SMART_GLASSES = new SmartGlassesItem(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("smart_node_container"), () ->
            SMART_NODE_CONTAINER = new SmartNodeContainerItem(new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("graph_visual_editor"), () ->
            GRAPH_VISUAL_EDITOR = new GraphVisualEditorItem(new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("directional_face_editor"), () ->
            DIRECTIONAL_FACE_EDITOR = new DirectionalFaceEditorItem(new Item.Properties().stacksTo(1))
        );
        event.register(Registries.ITEM, id("redstonelink_status_panel"), () ->
            REDSTONELINK_STATUS_PANEL = new StatePanelToolItem(new Item.Properties().stacksTo(1))
        );
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(RedstoneLink.MOD_ID, path);
    }
}
