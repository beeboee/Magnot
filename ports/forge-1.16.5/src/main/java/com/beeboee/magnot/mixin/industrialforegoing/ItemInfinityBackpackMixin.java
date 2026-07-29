package com.beeboee.magnot.mixin.industrialforegoing;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
                    target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterBackpackItems(
            World world,
            Class<T> entityClass,
            AxisAlignedBB box,
            ItemStack stack,
            World originalWorld,
            Entity source,
            int slotId,
            boolean selected
    ) {
        List<T> candidates = world.getEntitiesOfClass(entityClass, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        final ServerWorld serverWorld = (ServerWorld) world;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity)
                        || !magnot$isBlocked(serverWorld, source, (ItemEntity) candidate))
                .collect(Collectors.toList());
    }

    private static boolean magnot$isBlocked(ServerWorld world, Entity source, ItemEntity item) {
        if (source instanceof PlayerEntity) {
            return FerrousMagnetRules.blocksPlayerItemPull(world, (PlayerEntity) source, item);
        }
        return FerrousMagnetRules.blocksItemPull(world, source.position(), item);
    }
}
