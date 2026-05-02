package com.makomi;

import com.makomi.command.ModCommands;
import com.makomi.command.argument.ModCommandArgumentTypes;
import com.makomi.config.RedstoneLinkConfig;
import com.makomi.data.ChannelDispatchScheduler;
import com.makomi.data.CoreDispatchBatchScheduler;
import com.makomi.data.CrossChunkDispatchService;
import com.makomi.data.InternalDispatchDeltaProjector;
import com.makomi.data.LinkDispatchFilterService;
import com.makomi.data.LinkNodeLifecycleDispatchEvents;
import com.makomi.data.LinkNodeRetireEvents;
import com.makomi.data.NodeStateTraceService;
import com.makomi.data.StatePanelRecordingSessionService;
import com.makomi.data.input.InputPlaybackService;
import com.makomi.network.BenchCommandNetwork;
import com.makomi.network.ChunkActivatorNetwork;
import com.makomi.network.DirectionalFaceEditorNetwork;
import com.makomi.network.LinkFilterNetwork;
import com.makomi.network.PairingNetwork;
import com.makomi.network.QuickLinkNetwork;
import com.makomi.network.RepeaterNetwork;
import com.makomi.network.SmartNodeContainerNetwork;
import com.makomi.network.StatePanelNetwork;
import com.makomi.registry.ModBlockEntities;
import com.makomi.registry.ModBlocks;
import com.makomi.registry.ModItemGroups;
import com.makomi.registry.ModItems;
import com.makomi.registry.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.impl.client.ClientCompatBridge;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

@Mod(RedstoneLink.MOD_ID)
public class RedstoneLink {
    public static final String MOD_ID = "redstonelink";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RedstoneLink(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(ArgumentTypeRegistry::bootstrap);
        modEventBus.addListener(PayloadTypeRegistry::bootstrap);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModItemGroups.register(modEventBus);
        if (FMLEnvironment.dist.isClient()) {
            ClientCompatBridge.register(modEventBus);
        }

        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> CommandRegistrationCallback.EVENT.fire(event));
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) -> ServerLifecycleEvents.fireServerStarting(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerStartedEvent event) -> ServerLifecycleEvents.fireServerStarted(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerStoppingEvent event) -> ServerLifecycleEvents.fireServerStopping(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> ServerLifecycleEvents.fireServerStopped(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre event) -> ServerTickEvents.fireStartServerTick(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> ServerTickEvents.fireEndServerTick(event.getServer()));
        NeoForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                ServerEntityEvents.fireEntityLoad(event.getEntity(), level);
            }
        });
        NeoForge.EVENT_BUS.addListener((EntityLeaveLevelEvent event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                ServerEntityEvents.fireEntityUnload(event.getEntity(), level);
            }
        });
        NeoForge.EVENT_BUS.addListener((BlockEvent.BreakEvent event) -> {
            if (event.isCanceled() || !(event.getLevel() instanceof ServerLevel level)) {
                return;
            }
            PlayerBlockBreakEvents.fireAfter(
                level,
                event.getPlayer(),
                event.getPos(),
                event.getState(),
                level.getBlockEntity(event.getPos())
            );
        });
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                ServerPlayConnectionEvents.fireDisconnect(player.connection, player.server);
            }
        });

        initializeCommon();
    }

    private static void initializeCommon() {
        RedstoneLinkConfig.load();
        BenchCommandNetwork.register();
        PairingNetwork.register();
        QuickLinkNetwork.register();
        DirectionalFaceEditorNetwork.register();
        LinkFilterNetwork.register();
        ChunkActivatorNetwork.register();
        RepeaterNetwork.register();
        SmartNodeContainerNetwork.register();
        ModCommandArgumentTypes.register();
        ModCommands.register();
        InternalDispatchDeltaProjector.register();
        LinkNodeLifecycleDispatchEvents.register();
        LinkNodeRetireEvents.register();
        LinkDispatchFilterService.register();
        NodeStateTraceService.register();
        if (RedstoneLinkConfig.command().inputEnabled()) {
            InputPlaybackService.register();
        }
        CrossChunkDispatchService.register();
        ChannelDispatchScheduler.register();
        CoreDispatchBatchScheduler.register();
        StatePanelRecordingSessionService.register();
        StatePanelNetwork.register();
        LOGGER.info("RedstoneLink initialized");
    }
}
