package com.beeboee.magnot.mixin.artifacts;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPullFromEntity(Entity targetEntity, Vec3 motion, ServerLevel level, LivingEntity entity, int amplifier) {
        magnot$blockFerrousRegionPull(targetEntity, motion, level, entity);
    }

    @Redirect(
            method = "applyEffectTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPullFromItem(ItemEntity itemEntity, Vec3 motion, ServerLevel level, LivingEntity entity, int amplifier) {
        magnot$blockFerrousRegionPull(itemEntity, motion, level, entity);
    }

    private void magnot$blockFerrousRegionPull(Entity targetEntity, Vec3 motion, ServerLevel level, LivingEntity entity) {
        if (!(targetEntity instanceof ItemEntity itemEntity)) {
            targetEntity.setDeltaMovement(motion);
            return;
        }

        if (entity instanceof Player player) {
            if (FerrousMagnetRules.blocksPlayerMagnet(level, player, itemEntity.position())) {
                MagnotDebug.log("artifacts-block player={} item={} motion={}", player.getName().getString(), itemEntity.getId(), motion);
                return;
            }
        } else if (FerrousMagnetRules.blocksMagnet(level, entity.position(), itemEntity.position())) {
            MagnotDebug.log("artifacts-block source={} item={} motion={}", entity.getId(), itemEntity.getId(), motion);
            return;
        }

        targetEntity.setDeltaMovement(motion);
    }
}
