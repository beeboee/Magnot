package com.beeboee.magnot.mixin.portcompat;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "com.supermartijn642.itemcollectors.CollectorBlockEntity", remap = false)
abstract class ItemCollectorsMixin {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterCollectorItems(World world, Class<T> type, AxisAlignedBB box, Predicate<? super T> predicate) {
        List<T> candidates = world.getEntitiesOfClass(type, box, predicate);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        Vector3d source = magnot$center(((TileEntity) (Object) this).getBlockPos());
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverWorld, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }

    private static Vector3d magnot$center(BlockPos pos) {
        return new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }
}

@Pseudo
@Mixin(targets = "mob_grinding_utils.tile.TileEntityAbsorptionHopper", remap = false)
abstract class MobGrindingUtilsMixin {
    @Redirect(method = "getCaptureItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterHopperItems(World world, Class<T> type, AxisAlignedBB box, Predicate<? super T> predicate) {
        List<T> candidates = world.getEntitiesOfClass(type, box, predicate);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        BlockPos pos = ((TileEntity) (Object) this).getBlockPos();
        Vector3d source = new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverWorld, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", remap = false)
abstract class ModularRoutersMixin {
    @Redirect(method = "handleItemMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterRouterItems(World world, Class<T> type, AxisAlignedBB box, @Coerce Object router) {
        List<T> candidates = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        Vector3d source = box.getCenter();
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverWorld, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", remap = false)
abstract class MekanismMixin {
    @Inject(method = "pullItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterMekanismItem(PlayerEntity player, ItemEntity item, CallbackInfo ci) {
        if (item.level instanceof ServerWorld
                && FerrousMagnetRules.blocksPlayerItemPull((ServerWorld) item.level, player, item)) {
            ci.cancel();
        }
    }
}

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
abstract class DraconicEvolutionMixin {
    @Redirect(method = "updateMagnet", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterDraconicItems(World world, Class<T> type, AxisAlignedBB box, ItemStack stack, Entity source) {
        List<T> candidates = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !(source instanceof PlayerEntity) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        PlayerEntity player = (PlayerEntity) source;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverWorld, player, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = {"reliquary.item.FortuneCoinItem", "xreliquary.items.FortuneCoinItem", "xreliquary.items.FortuneCoin"}, remap = false)
abstract class ReliquaryMixin {
    @Redirect(method = "scanForEntitiesInRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterPlayerItems(World world, Class<T> type, AxisAlignedBB box, World originalWorld, PlayerEntity player, double distance) {
        List<T> candidates = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverWorld, player, (ItemEntity) candidate)).collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterTeleport(Entity entity, PlayerEntity player, CallbackInfo ci) {
        if (entity instanceof ItemEntity && player.level instanceof ServerWorld
                && FerrousMagnetRules.blocksPlayerItemPull((ServerWorld) player.level, player, (ItemEntity) entity)) {
            ci.cancel();
        }
    }
}

@Pseudo
@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", remap = false)
abstract class SophisticatedCoreMixin {
    @Unique private Vector3d magnot$source;
    @Unique private PlayerEntity magnot$playerSource;

    @Inject(method = "pickupItems", at = @At("HEAD"), require = 0)
    private void magnot$captureSource(Entity entity, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$playerSource = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        magnot$source = entity == null ? new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) : entity.position();
    }

    @Inject(method = "pickupItems", at = @At("RETURN"), require = 0)
    private void magnot$clearSource(Entity entity, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$source = null;
        magnot$playerSource = null;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterSophisticatedItem(PlayerEntity player, ItemEntity item, CallbackInfoReturnable<Boolean> cir) {
        if (!(item.level instanceof ServerWorld)) return;
        ServerWorld serverWorld = (ServerWorld) item.level;
        PlayerEntity sourcePlayer = magnot$playerSource == null ? player : magnot$playerSource;
        if (sourcePlayer != null && FerrousMagnetRules.blocksPlayerItemPull(serverWorld, sourcePlayer, item)) {
            cir.setReturnValue(false);
        } else if (sourcePlayer == null && magnot$source != null && FerrousMagnetRules.blocksItemPull(serverWorld, magnot$source, item)) {
            cir.setReturnValue(false);
        }
    }
}

@Pseudo
@Mixin(targets = "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", remap = false)
abstract class IndustrialForegoingBackpackMixin {
    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterInfinityBackpackItems(World world, Class<T> type, AxisAlignedBB box, ItemStack stack, World originalWorld, Entity source, int slot, boolean selected) {
        List<T> candidates = world.getEntitiesOfClass(type, box);
        if (!(world instanceof ServerWorld) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerWorld serverWorld = (ServerWorld) world;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || (source instanceof PlayerEntity
                    ? !FerrousMagnetRules.blocksPlayerItemPull(serverWorld, (PlayerEntity) source, (ItemEntity) candidate)
                    : !FerrousMagnetRules.blocksItemPull(serverWorld, source.position(), (ItemEntity) candidate)))
                .collect(Collectors.toList());
    }
}
