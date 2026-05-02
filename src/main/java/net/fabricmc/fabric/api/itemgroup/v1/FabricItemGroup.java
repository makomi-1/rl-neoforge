package net.fabricmc.fabric.api.itemgroup.v1;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class FabricItemGroup {
    private FabricItemGroup() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Component title = Component.empty();
        private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
        private DisplayItemsGenerator displayItems = (parameters, entries) -> {
        };

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder icon(Supplier<ItemStack> icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(DisplayItemsGenerator displayItems) {
            this.displayItems = displayItems;
            return this;
        }

        public CreativeModeTab build() {
            return CreativeModeTab.builder()
                .title(title)
                .icon(icon)
                .displayItems((parameters, output) -> displayItems.accept(parameters, new Entries() {
                    @Override
                    public void accept(ItemLike item, CreativeModeTab.TabVisibility visibility) {
                        output.accept(new ItemStack(item), visibility);
                    }

                    @Override
                    public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                        output.accept(stack, visibility);
                    }
                }))
                .build();
        }
    }

    @FunctionalInterface
    public interface DisplayItemsGenerator {
        void accept(CreativeModeTab.ItemDisplayParameters parameters, Entries entries);
    }

    public interface Entries {
        void accept(ItemLike item, CreativeModeTab.TabVisibility visibility);

        void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility);
    }
}
