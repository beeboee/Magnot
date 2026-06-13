package com.beeboee.magnot.mixin.ae2wtlib;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", remap = false)
public abstract class MagnetHandlerMixin {
    @Redirect(
            method = "handleMagnet(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V"
            )
    )
    private static void magnot$blockFerrousRegionPickup(ItemEntity itemEntity, Player player) {
        if (itemEntity.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksMagnet(serverLevel, player.getEyePosition(), itemEntity.position())) {
            return;
        }

        itemEntity.playerTouch(player);
    }
}
