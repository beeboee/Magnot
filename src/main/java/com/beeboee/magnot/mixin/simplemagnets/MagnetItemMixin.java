package com.beeboee.magnot.mixin.simplemagnets;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.supermartijn642.simplemagnets.MagnetItem", remap = false)
public abstract class MagnetItemMixin {
    @Redirect(
            method = {"inventoryTick", "inventoryUpdate"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPos(DDD)V"
            ),
            require = 0
    )
    private void magnot$blockSimpleMagnetTeleport(ItemEntity itemEntity, double x, double y, double z, ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (level.isClientSide()) {
            return;
        }
        if (entity instanceof Player player
                && itemEntity.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, FerrousMagnetRules.itemPullTarget(itemEntity))) {
            return;
        }

        itemEntity.setPos(x, y, z);
    }
}
