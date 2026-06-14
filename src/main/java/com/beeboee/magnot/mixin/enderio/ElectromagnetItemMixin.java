package com.beeboee.magnot.mixin.enderio;

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
import java.util.function.Predicate;

@Pseudo
@Mixin(targets = "com.enderio.enderio.content.tools.ElectromagnetItem", remap = false)
public abstract class ElectromagnetItemMixin {
    @Redirect(
            method = "onTickWhenActive",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            require = 0
    )
    private List<Entity> magnot$filterElectromagnetItems(Level level, Entity excluded, AABB box, Predicate<? super Entity> predicate, Player player, ItemStack stack, Level originalLevel, Entity entity, int slotId, boolean isSelected) {
        List<Entity> candidates = level.getEntities(excluded, box, predicate);
        if (level.isClientSide()) {
            return List.of();
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return candidates;
        }

        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, FerrousMagnetRules.itemPullTarget(item)))
                .toList();
    }
}
