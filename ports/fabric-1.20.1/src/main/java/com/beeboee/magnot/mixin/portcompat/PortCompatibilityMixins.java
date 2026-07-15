package com.beeboee.magnot.mixin.portcompat;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", remap = false)
abstract class Ae2wtlibMagnetHandlerMixin {
    @Redirect(
            method = "handleMagnet(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V",
                    remap = true
            ),
            require = 0
    )
    private static void magnot$filterAe2wtlibPickup(ItemEntity item, Player player) {
        if (item.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) {
            return;
        }
        item.playerTouch(player);
    }
}

@Pseudo
@Mixin(targets = "artifacts.item.wearable.belt.UniversalAttractorItem", remap = false)
abstract class ArtifactsUniversalAttractorMixin {
    @Redirect(
            method = "wornTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterArtifactsItems(
            Level level,
            Class<T> type,
            AABB box,
            LivingEntity wearer,
            ItemStack stack
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        Vec3 source = wearer.position().add(0.0D, 0.75D, 0.0D);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || (wearer instanceof Player player
                        ? !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)
                        : !FerrousMagnetRules.blocksItemPull(serverLevel, source, item)))
                .collect(Collectors.toList());
    }
}
