package net.fabricmc.fabric.api.command.v2;

import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ArgumentTypeRegistry {
    private static final List<PendingRegistration> PENDING = new ArrayList<>();

    private ArgumentTypeRegistry() {
    }

    public static synchronized <A extends ArgumentType<?>> void registerArgumentType(
        ResourceLocation id,
        Class<? extends A> argumentClass,
        ArgumentTypeInfo<?, ?> info
    ) {
        PENDING.add(new PendingRegistration(id, argumentClass, info));
    }

    public static synchronized void bootstrap(RegisterEvent event) {
        event.register(Registries.COMMAND_ARGUMENT_TYPE, helper -> {
            for (PendingRegistration pending : PENDING) {
                bindArgumentClass(pending.argumentClass(), pending.info());
                helper.register(pending.id(), pending.info());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static void bindArgumentClass(Class<?> argumentClass, ArgumentTypeInfo<?, ?> info) {
        try {
            Field byClassField = ArgumentTypeInfos.class.getDeclaredField("BY_CLASS");
            byClassField.setAccessible(true);
            Map<Class<?>, ArgumentTypeInfo<?, ?>> byClass =
                (Map<Class<?>, ArgumentTypeInfo<?, ?>>) byClassField.get(null);
            byClass.put(argumentClass, info);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Failed to bind custom command argument type: " + argumentClass.getName(), ex);
        }
    }

    private record PendingRegistration(
        ResourceLocation id,
        Class<?> argumentClass,
        ArgumentTypeInfo<?, ?> info
    ) {
    }
}
