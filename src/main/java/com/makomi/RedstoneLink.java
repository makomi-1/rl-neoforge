package com.makomi;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(RedstoneLink.MOD_ID)
public class RedstoneLink {
    public static final String MOD_ID = "redstonelink";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RedstoneLink(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("RedstoneLink NeoForge bootstrap initialized");
    }
}
