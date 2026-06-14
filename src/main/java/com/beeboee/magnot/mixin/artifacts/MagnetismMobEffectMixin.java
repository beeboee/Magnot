package com.beeboee.magnot.mixin.artifacts;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(targets = "artifacts.effect.MagnetismMobEffect", remap = false)
public abstract class MagnetismMobEffectMixin {
    @Redirect(
            method = "applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private List<ItemEntity> magnot$filterFerrousRegionBlockedItems(Level queriedLevel, Class<ItemEntity> entityClass, AABB bounds, LivingEntity entity, int amplifier) {
        List<ItemEntity> items = queriedLevel.getEntitiesOfClass(entityClass, bounds);
        if (!(queriedLevel instanceof ServerLevel serverLevel)) {
            return items;
        }

        Vec3 source = entity.position().add(0.0D, 0.75D, 0.0D);
        List<ItemEntity> filtered = null;

        for (int index = 0; index < items.size(); index++) {
            ItemEntity item = items.get(index);
            if (magnot$blocksArtifactsPull(serverLevel, entity, source, item)) {
                if (filtered == null) {
                    filtered = new ArrayList<>(items.size());
                    filtered.addAll(items.subList(0, index));
                }
                continue;
            }

            if (filtered != null) {
                filtered.add(item);
            }
        }

        return filtered == null ? items : filtered;
    }

    @Redirect(
            method = "applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPullFromEntity(Entity targetEntity, Vec3 motion, LivingEntity entity, int amplifier) {
        magnot$blockFerrousRegionPull(targetEntity, motion, entity);
    }

    @Redirect(
            method = "applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionPullFromItem(ItemEntity itemEntity, Vec3 motion, LivingEntity entity, int amplifier) {
        magnot$blockFerrousRegionPull(itemEntity, motion, entity);
    }

    private void magnot$blockFerrousRegionPull(Entity targetEntity, Vec3 motion, LivingEntity entity) {
        if (!(targetEntity instanceof ItemEntity itemEntity)) {
            targetEntity.setDeltaMovement(motion);
            return;
        }

        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            targetEntity.setDeltaMovement(motion);
            return;
        }

        Vec3 source = entity.position().add(0.0D, 0.75D, 0.0D);
        if (magnot$blocksArtifactsPull(serverLevel, entity, source, itemEntity)) {
            return;
        }

        targetEntity.setDeltaMovement(motion);
    }

    private boolean magnot$blocksArtifactsPull(ServerLevel level, LivingEntity entity, Vec3 source, ItemEntity itemEntity) {
        Vec3 target = FerrousMagnetRules.itemPullTarget(itemEntity);
        boolean blocked;

        if (entity instanceof Player player) {
            blocked = FerrousMagnetRules.blocksPlayerMagnet(level, player, target);
        } else {
            blocked = FerrousMagnetRules.blocksMagnet(level, source, target);
        }

        if (!blocked) {
            return false;
        }

        if (entity instanceof Player player) {
            MagnotDebug.log("artifacts-filter player={} item={} source={} target={}", player.getName().getString(), itemEntity.getId(), source, target);
        } else {
            MagnotDebug.log("artifacts-filter source={} item={} sourcePos={} target={}", entity.getId(), itemEntity.getId(), source, target);
        }
        return true;
    }
}
