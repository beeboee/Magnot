package com.beeboee.magnot.mixin.reliquary;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "xreliquary.items.FortuneCoinItem", remap = false)
public abstract class FortuneCoinItemMixin {
    @Redirect(
            method = "scanForEntitiesInRange",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPlayerItems(World world, Class<T> type,
                                                                AxisAlignedBB box, World originalWorld,
                                                                PlayerEntity player, double distance) {
        List<T> candidates = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverWorld, player, (ItemEntity) candidate))
                .collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterTeleport(Entity entity, PlayerEntity player, CallbackInfo ci) {
        if (entity instanceof ItemEntity && player.level instanceof ServerWorld
                && FerrousMagnetRules.blocksPlayerItemPull((ServerWorld) player.level, player, (ItemEntity) entity)) {
            ci.cancel();
        }
    }
}
