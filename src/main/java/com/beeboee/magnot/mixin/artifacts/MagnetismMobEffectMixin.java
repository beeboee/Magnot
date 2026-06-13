package com.beeboee.magnot.mixin.artifacts;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "artifacts.effect.MagnetismMobEffect", remap = false)
public abstract class MagnetismMobEffectMixin {
    @Redirect(
            method = "applyEffectTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPull(ItemEntity itemEntity, Vec3 motion, ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (FerrousMagnetRules.blocksPlayerMagnet(level, player, itemEntity.position())) {
                return;
            }
        } else if (FerrousMagnetRules.blocksMagnet(level, entity.position(), itemEntity.position())) {
            return;
        }

        itemEntity.setDeltaMovement(motion);
    }
}
