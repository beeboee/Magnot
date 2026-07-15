package com.beeboee.magnot.mixin.artifacts;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Pseudo
@Mixin(targets = "artifacts.effect.MagnetismMobEffect", remap = false)
public abstract class MagnetismMobEffectMixin {
    @Unique
    private FerrousMagnetRules.MagnetQueryContext magnot$queryContext;

    @Unique
    private final Map<ItemEntity, Boolean> magnot$decisions = new IdentityHashMap<>();

    @Inject(
            method = "applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At("HEAD"),
            require = 0
    )
    private void magnot$beginMagnetPass(LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        magnot$decisions.clear();
        if (entity.level() instanceof ServerLevel serverLevel) {
            magnot$queryContext = magnot$createQuery(serverLevel, entity);
        } else {
            magnot$queryContext = null;
        }
    }

    @Inject(
            method = "applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At("RETURN"),
            require = 0
    )
    private void magnot$endMagnetPass(LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        magnot$queryContext = null;
        magnot$decisions.clear();
    }

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

        FerrousMagnetRules.MagnetQueryContext query = magnot$queryContext;
        if (query == null) {
            query = magnot$createQuery(serverLevel, entity);
            magnot$queryContext = query;
        }

        List<ItemEntity> filtered = null;
        for (int index = 0; index < items.size(); index++) {
            ItemEntity item = items.get(index);
            boolean blocked = query.blocks(item);
            magnot$decisions.put(item, blocked);
            if (blocked) {
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
        magnot$applyMotionIfAllowed(targetEntity, motion, entity);
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
        magnot$applyMotionIfAllowed(itemEntity, motion, entity);
    }

    @Unique
    private void magnot$applyMotionIfAllowed(Entity targetEntity, Vec3 motion, LivingEntity entity) {
        if (!(targetEntity instanceof ItemEntity itemEntity)
                || !(entity.level() instanceof ServerLevel serverLevel)) {
            targetEntity.setDeltaMovement(motion);
            return;
        }

        Boolean blocked = magnot$decisions.get(itemEntity);
        if (blocked == null) {
            FerrousMagnetRules.MagnetQueryContext query = magnot$queryContext;
            if (query == null) {
                query = magnot$createQuery(serverLevel, entity);
                magnot$queryContext = query;
            }
            blocked = query.blocks(itemEntity);
            magnot$decisions.put(itemEntity, blocked);
        }

        if (!blocked) {
            targetEntity.setDeltaMovement(motion);
        }
    }

    @Unique
    private static FerrousMagnetRules.MagnetQueryContext magnot$createQuery(ServerLevel level, LivingEntity entity) {
        if (entity instanceof Player player) {
            return FerrousMagnetRules.playerContext(level, player);
        }
        return FerrousMagnetRules.sourceContext(level, entity.position().add(0.0D, 0.75D, 0.0D));
    }
}
