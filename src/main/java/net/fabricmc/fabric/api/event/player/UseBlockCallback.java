package net.fabricmc.fabric.api.event.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Fabric 客户端右键方块回调兼容层。
 */
public final class UseBlockCallback {
    public static final Event EVENT = new Event();

    private UseBlockCallback() {
    }

    public static InteractionResult fire(
        Player player,
        Level level,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        for (Callback callback : EVENT.listeners()) {
            InteractionResult result = callback.interact(player, level, hand, hitResult);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Callback {
        InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);
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
