package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", remap = false)
public abstract class BlackHoleBandMixin {
    @Redirect(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterInventoryPull(
            Level level,
            Class<T> entityClass,
            AABB box,
            ItemStack stack,
            Level originalLevel,
            Entity owner,
            int slot,
            boolean held
    ) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel)
                || !(owner instanceof Player player)
                || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        return magnot$filterPlayer(serverLevel, player, candidates);
    }

    @Redirect(
            method = "updateInAlchBag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterBagPull(
            Level level,
            Class<T> entityClass,
            AABB box,
            IItemHandler inventory,
            Player player,
            ItemStack stack
    ) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel)
                || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        return magnot$filterPlayer(serverLevel, player, candidates);
    }

    @Redirect(
            method = "updateInPedestal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPedestalPull(
            Level level,
            Class<T> entityClass,
            AABB box,
            Predicate<? super T> predicate,
            ItemStack stack,
            Level originalLevel,
            BlockPos pos,
            BlockEntity pedestal
    ) {
        return magnot$filterBlock(level, entityClass, box, predicate, pos);
    }

    @Redirect(
            method = "updateInAlchChest",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterChestPull(
            Level level,
            Class<T> entityClass,
            AABB box,
            Predicate<? super T> predicate,
            Level originalLevel,
            BlockPos pos,
            ItemStack stack
    ) {
        return magnot$filterBlock(level, entityClass, box, predicate, pos);
    }

    private static <T extends Entity> List<T> magnot$filterPlayer(
            ServerLevel level,
            Player player,
            List<T> candidates
    ) {
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(level, player, item))
                .collect(Collectors.toList());
    }

    private static <T extends Entity> List<T> magnot$filterBlock(
            Level level,
            Class<T> entityClass,
            AABB box,
            Predicate<? super T> predicate,
            BlockPos pos
    ) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box, predicate);
        if (!(level instanceof ServerLevel serverLevel)
                || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .collect(Collectors.toList());
    }
}
