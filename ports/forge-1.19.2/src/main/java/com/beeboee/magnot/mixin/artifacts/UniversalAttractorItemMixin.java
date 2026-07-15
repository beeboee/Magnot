package com.beeboee.magnot.mixin.artifacts;

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
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Method;
import java.util.List;

@Pseudo
@Mixin(targets = "artifacts.common.item.curio.belt.UniversalAttractorItem", remap = false)
public abstract class UniversalAttractorItemMixin {
    @Redirect(
            method = "curioTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterAttractedItems(Level level, Class<T> type, AABB box,
                                                                    @Coerce Object slotContext, ItemStack stack) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        Player player = magnot$player(slotContext);
        if (!(level instanceof ServerLevel serverLevel) || player == null || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item))
                .toList();
    }

    private static Player magnot$player(Object slotContext) {
        try {
            Method entity = slotContext.getClass().getMethod("entity");
            Object value = entity.invoke(slotContext);
            return value instanceof Player ? (Player) value : null;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
