package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.region.FerrousMagnetRules;
import moze_intel.projecte.utils.WorldHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
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
        Vec3 itemPosition = entity.position();

        if (entity.level() instanceof ServerLevel serverLevel) {
            if (FerrousMagnetRules.blocksMagnet(serverLevel, target, itemPosition)) {
                return;
            }

            WorldHelper.gravitateEntityTowards(entity, target);
            return;
        }

        if (ClientFerrousRegionStore.closestIntersecting(target, itemPosition).isPresent()) {
            return;
        }

        if (ModList.get().isLoaded("sable") && MagnotSableClientCompat.blocksMagnet(entity.level(), target, itemPosition)) {
            return;
        }

        WorldHelper.gravitateEntityTowards(entity, target);
    }
}
