package com.beeboee.magnot.mixin.projecte;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", remap = false)
public abstract class BlackHoleBandMixin {
    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> inventory(Level level, Class<T> type, AABB box, ItemStack stack, Level original, Entity owner, int slot, boolean held) {
        List<T> found = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !(owner instanceof Player) || !ItemEntity.class.isAssignableFrom(type)) return found;
        ServerLevel server = (ServerLevel) level; Player player = (Player) owner;
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksPlayerItemPull(server, player, (ItemEntity) e)).collect(Collectors.toList());
    }

    @Redirect(method = "updateInAlchBag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> bag(Level level, Class<T> type, AABB box, IItemHandler inv, Player player, ItemStack stack) {
        List<T> found = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return found;
        ServerLevel server = (ServerLevel) level;
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksPlayerItemPull(server, player, (ItemEntity) e)).collect(Collectors.toList());
    }

    @Redirect(method = "updateInPedestal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> pedestal(Level level, Class<T> type, AABB box, Predicate<? super T> predicate, ItemStack stack, Level original, BlockPos pos, BlockEntity pedestal) { return block(level, type, box, predicate, pos); }

    @Redirect(method = "updateInAlchChest", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> chest(Level level, Class<T> type, AABB box, Predicate<? super T> predicate, Level original, BlockPos pos, ItemStack stack) { return block(level, type, box, predicate, pos); }

    private static <T extends Entity> List<T> block(Level level, Class<T> type, AABB box, Predicate<? super T> predicate, BlockPos pos) {
        List<T> found = level.getEntitiesOfClass(type, box, predicate);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return found;
        ServerLevel server = (ServerLevel) level; Vec3 source = Vec3.atCenterOf(pos);
        return found.stream().filter(e -> !(e instanceof ItemEntity) || !FerrousMagnetRules.blocksItemPull(server, source, (ItemEntity) e)).collect(Collectors.toList());
    }
}
