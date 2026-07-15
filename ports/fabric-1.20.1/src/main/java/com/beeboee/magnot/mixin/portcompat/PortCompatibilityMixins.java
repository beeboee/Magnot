package com.beeboee.magnot.mixin.portcompat;

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
abstract class Ae2wtlibMagnetHandlerMixin {
    @Redirect(
            method = "handleMagnet(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V",
                    remap = true
            ),
            require = 0
    )
    private static void magnot$filterAe2wtlibPickup(ItemEntity item, Player player) {
        if (item.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) {
            return;
        }
        item.playerTouch(player);
    }
}
