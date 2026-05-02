package net.fabricmc.fabric.api.event.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Fabric 客户端左键方块回调兼容层。
 */
public final class AttackBlockCallback {
    public static final Event EVENT = new Event();

    private AttackBlockCallback() {
    }

    public static InteractionResult fire(
        Player player,
        Level level,
        InteractionHand hand,
        BlockPos blockPos,
        Direction direction
    ) {
        for (Callback callback : EVENT.listeners()) {
            InteractionResult result = callback.interact(player, level, hand, blockPos, direction);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Callback {
        InteractionResult interact(Player player, Level level, InteractionHand hand, BlockPos blockPos, Direction direction);
    }

    public static final class Event {
        private final List<Callback> listeners = new CopyOnWriteArrayList<>();

        public void register(Callback callback) {
            listeners.add(callback);
        }

        private List<Callback> listeners() {
            return listeners;
        }
    }
}
