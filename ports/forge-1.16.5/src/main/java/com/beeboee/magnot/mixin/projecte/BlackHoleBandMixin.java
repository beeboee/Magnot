package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", remap = false)
public abstract class BlackHoleBandMixin {
    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> inventory(World world, Class<T> type, AxisAlignedBB box, ItemStack stack, World original, Entity owner, int slot, boolean held) {
        List<T> found = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !(owner instanceof PlayerEntity) || !ItemEntity.class.isAssignableFrom(type)) return found;
        final ServerWorld server = (ServerWorld) world; final PlayerEntity player = (PlayerEntity) owner;
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksPlayerItemPull(server, player, (ItemEntity) e)).collect(Collectors.toList());
    }

    @Redirect(method = "updateInAlchBag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> bag(World world, Class<T> type, AxisAlignedBB box, IItemHandler inv, PlayerEntity player, ItemStack stack) {
        List<T> found = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return found;
        final ServerWorld server = (ServerWorld) world;
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksPlayerItemPull(server, player, (ItemEntity) e)).collect(Collectors.toList());
    }

    @Redirect(method = "updateInPedestal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> pedestal(World world, Class<T> type, AxisAlignedBB box, World original, BlockPos pos) { return block(world, type, box, pos); }

    @Redirect(method = "updateInAlchChest", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> chest(World world, Class<T> type, AxisAlignedBB box, World original, BlockPos pos, ItemStack stack) { return block(world, type, box, pos); }

    private static <T extends Entity> List<T> block(World world, Class<T> type, AxisAlignedBB box, BlockPos pos) {
        List<T> found = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return found;
        final ServerWorld server = (ServerWorld) world;
        final Vector3d source = new Vector3d(pos.getX() + .5D, pos.getY() + .5D, pos.getZ() + .5D);
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksItemPull(server, source, (ItemEntity) e)).collect(Collectors.toList());
    }
}
