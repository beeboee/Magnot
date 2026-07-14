package com.beeboee.magnot.client;

import com.beeboee.magnot.item.FieldAugmenterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public final class FieldAugmenterItemDecorator implements IItemDecorator {
    public static final FieldAugmenterItemDecorator INSTANCE = new FieldAugmenterItemDecorator();

    private FieldAugmenterItemDecorator() {
    }

    @Override
    public boolean render(GuiGraphics graphics, Font font, ItemStack augmenter, int xOffset, int yOffset) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return false;
        }

        ItemStack stored = FieldAugmenterItem.getStoredItem(augmenter, minecraft.level.registryAccess());
        if (stored.isEmpty()) {
            return false;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(xOffset + 8.0F, yOffset, 200.0F);
        graphics.pose().scale(0.5F, 0.5F, 1.0F);
        graphics.renderItem(stored, 0, 0);
        graphics.pose().popPose();

        int marker = FieldAugmenterItem.isWhitelistMode(augmenter) ? 0xFFFFFFFF : 0xFF111111;
        graphics.fill(xOffset, yOffset, xOffset + 4, yOffset + 4, 0xFF9A9A9A);
        graphics.fill(xOffset + 1, yOffset + 1, xOffset + 3, yOffset + 3, marker);
        return true;
    }
}
