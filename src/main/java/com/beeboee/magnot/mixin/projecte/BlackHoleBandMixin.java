package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", remap = false)
public abstract class BlackHoleBandMixin {
    @Redirect(
            method = {
                    "inventoryTick",
                    "updateInPedestal",
                    "updateInAlchChest",
                    "updateInAlchBag"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lmoze_intel/projecte/utils/WorldHelper;gravitateEntityTowards(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private void magnot$blockFerrousRegionPull(Entity entity, Vec3 target) {
        if (entity.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksMagnet(serverLevel, target, entity.position())) {
            return;
        }

        moze_intel.projecte.utils.WorldHelper.gravitateEntityTowards(entity, target);
    }
}
