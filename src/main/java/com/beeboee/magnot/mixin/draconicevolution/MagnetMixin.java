package com.beeboee.magnot.mixin.draconicevolution;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
public abstract class MagnetMixin {
    @Redirect(
            method = "updateMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterFerrousRegionCandidates(Level level, Class<T> entityClass, AABB box, ItemStack stack, Entity entity) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(entity instanceof Player player)
                || !(level instanceof ServerLevel serverLevel)
                || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, FerrousMagnetRules.itemPullTarget(item)))
                .toList();
    }

    @Redirect(
            method = "updateMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setPos(DDD)V"
            ),
            require = 0
    )
    private void magnot$blockFerrousRegionTeleport(ItemEntity itemEntity, double x, double y, double z, ItemStack stack, Entity entity) {
        if (entity instanceof Player player
                && itemEntity.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, FerrousMagnetRules.itemPullTarget(itemEntity))) {
            return;
        }

        itemEntity.setPos(x, y, z);
    }
}
