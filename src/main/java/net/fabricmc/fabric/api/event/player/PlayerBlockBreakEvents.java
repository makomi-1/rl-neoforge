package net.fabricmc.fabric.api.event.player;

import net.fabricmc.fabric.impl.base.SimpleEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PlayerBlockBreakEvents {
    public static final SimpleEvent<After> AFTER = new SimpleEvent<>();

    private PlayerBlockBreakEvents() {
    }

    public static void fireAfter(
        ServerLevel level,
        Player player,
        BlockPos pos,
        BlockState state,
        BlockEntity blockEntity
    ) {
        AFTER.fire(callback -> callback.afterBlockBreak(level, player, pos, state, blockEntity));
    }

    @FunctionalInterface
    public interface After {
        void afterBlockBreak(ServerLevel level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity);
    }
}
