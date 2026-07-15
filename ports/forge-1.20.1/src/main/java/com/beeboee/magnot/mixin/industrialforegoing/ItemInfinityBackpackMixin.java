package com.beeboee.magnot.mixin.industrialforegoing;

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
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", remap = false)
public abstract class ItemInfinityBackpackMixin {
    @Redirect(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterBackpackItems(Level level, Class<T> entityClass,
                                                                  AABB box, ItemStack stack,
                                                                  Level originalLevel, Entity source,
                                                                  int slotId, boolean isSelected) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !magnot$isProtected(serverLevel, source, item))
                .collect(Collectors.toList());
    }

    private static boolean magnot$isProtected(ServerLevel level, Entity source, ItemEntity item) {
        if (source instanceof Player player) {
            return FerrousMagnetRules.blocksPlayerItemPull(level, player, item);
        }
        return FerrousMagnetRules.blocksItemPull(level, source.position(), item);
    }
}
