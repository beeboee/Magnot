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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterBackpackItems(Level level, Class<T> type, AABB box,
                                                                  ItemStack stack, Level originalLevel,
                                                                  Entity source, int slot, boolean selected) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        ServerLevel serverLevel = (ServerLevel) level;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity)
                        || !magnot$isProtected(serverLevel, source, (ItemEntity) candidate))
                .collect(Collectors.toList());
    }

    private static boolean magnot$isProtected(ServerLevel level, Entity source, ItemEntity item) {
        if (source instanceof Player) {
            return FerrousMagnetRules.blocksPlayerItemPull(level, (Player) source, item);
        }
        return FerrousMagnetRules.blocksItemPull(level, source.position(), item);
    }
}
