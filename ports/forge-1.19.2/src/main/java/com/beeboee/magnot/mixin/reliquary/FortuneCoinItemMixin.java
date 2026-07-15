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
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "reliquary.items.FortuneCoinItem", remap = false)
public abstract class FortuneCoinItemMixin {
    @Redirect(method = "scanForEntitiesInRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterPlayerItems(Level level, Class<T> type, AABB box, Level originalLevel, Player player, double distance) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, (ItemEntity) candidate)).collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterTeleport(Entity entity, Player player, CallbackInfo ci) {
        if (entity instanceof ItemEntity && player.level instanceof ServerLevel
                && FerrousMagnetRules.blocksPlayerItemPull((ServerLevel) player.level, player, (ItemEntity) entity)) ci.cancel();
    }

    @Redirect(method = "pickupItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterPedestalItems(Level level, Class<T> type, AABB box, @Coerce Object pedestal, Level originalLevel, BlockPos pos) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}
