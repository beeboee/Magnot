package com.beeboee.magnot.mixin.actuallyadditions;

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
@Mixin(targets = "de.ellpeck.actuallyadditions.mod.items.ItemMagnetRing", remap = false)
public abstract class ItemMagnetRingMixin {
    @Redirect(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterMagnetRingItems(
            Level level,
            Class<T> entityClass,
            AABB box,
            ItemStack stack,
            Level originalLevel,
            Entity source,
            int slot,
            boolean selected
    ) {
        if (!(level instanceof ServerLevel serverLevel)
                || !(source instanceof Player player)
                || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return level.getEntitiesOfClass(entityClass, box);
        }

        FerrousMagnetRules.MagnetQueryContext query = FerrousMagnetRules.playerContext(serverLevel, player);
        query.prepare(box);
        if (query.isUnrestricted()) {
            return level.getEntitiesOfClass(entityClass, box);
        }

        return level.getEntitiesOfClass(
                entityClass,
                box,
                candidate -> !(candidate instanceof ItemEntity item) || !query.blocks(item)
        );
    }
}
