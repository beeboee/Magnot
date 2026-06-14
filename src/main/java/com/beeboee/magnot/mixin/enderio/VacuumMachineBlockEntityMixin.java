package com.beeboee.magnot.mixin.enderio;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Predicate;

@Pseudo
@Mixin(targets = "com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity", remap = false)
public abstract class VacuumMachineBlockEntityMixin {
    @Shadow
    private List<WeakReference> entities;

    @Redirect(
            method = "getEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterVacuumMachineItems(Level level, Class<T> entityClass, AABB box, Predicate<? super T> predicate, Level originalLevel, BlockPos pos, int range, Predicate<T> filter) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box, predicate);
        if (level.isClientSide()) {
            return List.of();
        }
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        Vec3 source = magnot$center(pos);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .toList();
    }

    @Inject(
            method = "attractEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    shift = At.Shift.BEFORE
            ),
            require = 0
    )
    private void magnot$removeBlockedCachedVacuumItems(Level level, BlockPos pos, int range, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 source = magnot$center(pos);
        entities.removeIf(reference -> {
            Object entity = reference.get();
            return entity instanceof ItemEntity item
                    && FerrousMagnetRules.blocksItemPull(serverLevel, source, item);
        });
    }

    private static Vec3 magnot$center(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }
}
