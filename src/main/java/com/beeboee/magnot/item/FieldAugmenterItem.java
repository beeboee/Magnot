package com.beeboee.magnot.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class FieldAugmenterItem extends Item {
    private static final String STORED_FILTER = "MagnotStoredFilter";
    private static final String WHITELIST_MODE = "MagnotWhitelistMode";

    public FieldAugmenterItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack augmenter, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        }

        HolderLookup.Provider registries = player.level().registryAccess();
        ItemStack stored = getStoredItem(augmenter, registries);
        ItemStack slotted = slot.getItem();

        if (stored.isEmpty()) {
            if (slotted.isEmpty() || slotted.getItem() instanceof FieldAugmenterItem || !slot.mayPickup(player)) {
                return false;
            }

            setStoredItem(augmenter, slotted.copyWithCount(1), registries);
            slotted.shrink(1);
            slot.setChanged();
            return true;
        }

        if (!slotted.isEmpty() || !slot.mayPlace(stored)) {
            return false;
        }

        slot.set(stored.copy());
        clearStoredItem(augmenter);
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack augmenter,
            ItemStack carried,
            Slot slot,
            ClickAction action,
            Player player,
            SlotAccess carriedAccess
    ) {
        if (action != ClickAction.SECONDARY) {
            return false;
        }

        HolderLookup.Provider registries = player.level().registryAccess();
        ItemStack stored = getStoredItem(augmenter, registries);

        if (stored.isEmpty()) {
            if (carried.isEmpty() || carried.getItem() instanceof FieldAugmenterItem) {
                return false;
            }

            setStoredItem(augmenter, carried.copyWithCount(1), registries);
            carried.shrink(1);
            return true;
        }

        if (!carried.isEmpty() || !carriedAccess.set(stored.copy())) {
            return false;
        }

        clearStoredItem(augmenter);
        return true;
    }

    public static ItemStack getStoredItem(ItemStack augmenter, HolderLookup.Provider registries) {
        CustomData data = augmenter.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = data.copyTag();
        if (!tag.contains(STORED_FILTER, Tag.TAG_COMPOUND)) {
            return ItemStack.EMPTY;
        }

        return ItemStack.parseOptional(registries, tag.getCompound(STORED_FILTER));
    }

    public static boolean hasStoredItem(ItemStack augmenter, HolderLookup.Provider registries) {
        return !getStoredItem(augmenter, registries).isEmpty();
    }

    public static boolean isWhitelistMode(ItemStack augmenter) {
        CustomData data = augmenter.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBoolean(WHITELIST_MODE);
    }

    public static void toggleMode(ItemStack augmenter) {
        CompoundTag tag = copyCustomData(augmenter);
        tag.putBoolean(WHITELIST_MODE, !tag.getBoolean(WHITELIST_MODE));
        CustomData.set(DataComponents.CUSTOM_DATA, augmenter, tag);
    }

    public static Component modeMessage(ItemStack augmenter) {
        return Component.translatable(
                isWhitelistMode(augmenter)
                        ? "message.magnot.filter_mode_allow"
                        : "message.magnot.filter_mode_block"
        );
    }

    private static void setStoredItem(ItemStack augmenter, ItemStack stored, HolderLookup.Provider registries) {
        CompoundTag tag = copyCustomData(augmenter);
        tag.put(STORED_FILTER, stored.copyWithCount(1).saveOptional(registries));
        CustomData.set(DataComponents.CUSTOM_DATA, augmenter, tag);
    }

    private static void clearStoredItem(ItemStack augmenter) {
        CompoundTag tag = copyCustomData(augmenter);
        tag.remove(STORED_FILTER);
        CustomData.set(DataComponents.CUSTOM_DATA, augmenter, tag);
    }

    private static CompoundTag copyCustomData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }
}
