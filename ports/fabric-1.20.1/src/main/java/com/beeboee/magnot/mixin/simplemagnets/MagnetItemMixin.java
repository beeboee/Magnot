package com.beeboee.magnot.mixin.simplemagnets;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Pseudo
@Mixin(targets = "com.supermartijn642.simplemagnets.MagnetItem", remap = false)
public abstract class MagnetItemMixin {
    @Redirect(
            method = {"inventoryTick", "inventoryUpdate"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterItems(Level level, EntityTypeTest<Entity, T> type, AABB box,
                                                          Predicate<? super T> predicate, ItemStack stack,
                                                          Level originalLevel, Entity source, int slot, boolean selected) {
        List<T> candidates = level.getEntities(type, box, predicate);
        if (!(level instanceof ServerLevel serverLevel) || !(source instanceof Player player)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item))
                .toList();
    }
}
