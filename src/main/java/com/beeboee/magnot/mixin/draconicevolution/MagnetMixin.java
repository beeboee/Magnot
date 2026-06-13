package com.beeboee.magnot.mixin.draconicevolution;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
public abstract class MagnetMixin {
    @Redirect(
            method = "updateMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPos(DDD)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionTeleport(ItemEntity itemEntity, double x, double y, double z, ItemStack stack, Entity entity) {
        if (entity instanceof Player player
                && itemEntity.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, itemEntity.position())) {
            return;
        }

        itemEntity.setPos(x, y, z);
    }
}
