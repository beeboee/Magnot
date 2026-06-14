package com.beeboee.magnot.mixin.reliquary;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import reliquary.api.IPedestal;

import java.util.List;

@Pseudo
@Mixin(targets = "reliquary.item.FortuneCoinItem", remap = false)
public abstract class FortuneCoinItemMixin {
    @Redirect(
            method = "scanForEntitiesInRange",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPlayerFortuneCoinCandidates(Level level, Class<T> entityClass, AABB box, Level originalLevel, Player player, double distance) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, FerrousMagnetRules.itemPullTarget(item)))
                .toList();
    }

    @Redirect(
            method = "pickupItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPedestalFortuneCoinCandidates(Level level, Class<T> entityClass, AABB box, IPedestal pedestal, Level originalLevel, BlockPos pedestalPos) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        Vec3 source = new Vec3(pedestalPos.getX() + 0.5D, pedestalPos.getY() + 0.5D, pedestalPos.getZ() + 0.5D);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .toList();
    }
}
