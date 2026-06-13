package com.beeboee.magnot.mixin.botania;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "vazkii.botania.common.item.equipment.bauble.RingOfMagnetizationItem", remap = false)
public abstract class RingOfMagnetizationItemMixin {
    @Redirect(
            method = "onWornTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/botania/common/helper/MathHelper;setEntityMotionFromVector(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;F)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPull(Entity entity, Vec3 target, float modifier, ItemStack stack, LivingEntity living) {
        if (entity instanceof ItemEntity itemEntity && entity.level() instanceof ServerLevel serverLevel) {
            if (living instanceof Player player) {
                if (FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, itemEntity.position())) {
                    return;
                }
            } else if (FerrousMagnetRules.blocksMagnet(serverLevel, living.position(), itemEntity.position())) {
                return;
            }
        }

        Vec3 entityCenter = entity.getBoundingBox().getCenter();
        Vec3 motion = target.subtract(entityCenter);
        if (motion.length() > 1.0D) {
            motion = motion.normalize();
        }
        entity.setDeltaMovement(motion.scale(modifier));
    }
}
