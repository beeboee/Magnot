package com.beeboee.magnot.mixin.itemcollectors;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Pseudo
@Mixin(targets = "com.supermartijn642.itemcollectors.CollectorBlockEntity", remap = false)
public abstract class CollectorBlockEntityMixin {
    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterCollectorItems(Level level, Class<T> entityClass, AABB box, Predicate<? super T> predicate) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box, predicate);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        BlockEntity blockEntity = (BlockEntity)(Object)this;
        Vec3 source = magnot$center(blockEntity.getBlockPos());
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .toList();
    }

    private static Vec3 magnot$center(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }
}
