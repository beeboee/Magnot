package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.region.FerrousMagnetRules;
import com.beeboee.magnot.region.FerrousRegion;
import moze_intel.projecte.utils.WorldHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
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
            boolean blocked = entity instanceof ItemEntity item
                    ? FerrousMagnetRules.blocksItemPull(serverLevel, target, item)
                    : FerrousMagnetRules.blocksMagnet(serverLevel, target, itemPosition);
            if (blocked) {
                return;
            }

            WorldHelper.gravitateEntityTowards(entity, target);
            return;
        }

        if (entity instanceof ItemEntity item) {
            if (magnot$clientBlocksItemPull(item, target)) {
                return;
            }
        } else if (magnot$clientBlocksMagnet(entity, target, itemPosition)) {
            return;
        }

        WorldHelper.gravitateEntityTowards(entity, target);
    }

    private boolean magnot$clientBlocksItemPull(ItemEntity item, Vec3 target) {
        Vec3 itemTarget = FerrousMagnetRules.itemPullTarget(item);

        boolean worldBlocked = ClientFerrousRegionStore.regions().stream()
                .filter(FerrousRegion::isWorldRegion)
                .anyMatch(region -> region.blocksItemPull(item.level(), target, itemTarget, item.getItem()));
        if (worldBlocked) {
            return true;
        }

        return ModList.get().isLoaded("sable") && MagnotSableClientCompat.blocksItemPull(item.level(), target, item);
    }

    private boolean magnot$clientBlocksMagnet(Entity entity, Vec3 target, Vec3 itemPosition) {
        if (ClientFerrousRegionStore.closestIntersecting(target, itemPosition).isPresent()) {
            return true;
        }

        return ModList.get().isLoaded("sable") && MagnotSableClientCompat.blocksMagnet(entity.level(), target, itemPosition);
    }
}
