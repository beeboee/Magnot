package com.beeboee.magnot.mixin.portcompat;

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
@Mixin(targets = "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", remap = false)
abstract class Ae2wtlibMagnetHandlerMixin {
    @Redirect(method = "handleMagnet(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V", remap = true), require = 0)
    private static void magnot$filterAe2wtlibPickup(ItemEntity item, Player player) {
        if (item.level instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) return;
        item.playerTouch(player);
    }
}

@Pseudo
@Mixin(targets = "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", remap = false)
abstract class MekanismMagneticAttractionMixin {
    @Inject(method = "pullItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterMekanismItem(Player player, ItemEntity item, CallbackInfo ci) {
        if (item.level instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) ci.cancel();
    }
}

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
abstract class DraconicEvolutionMagnetMixin {
    @Redirect(method = "updateMagnet", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> magnot$filterDraconicItems(Level level, Class<T> type, AABB box, ItemStack stack, Entity source) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !(source instanceof Player player) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity item)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "reliquary.items.FortuneCoinItem", remap = false)
abstract class ReliquaryFortuneCoinMixin {
    @Redirect(method = "scanForEntitiesInRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> magnot$filterPlayerItems(Level level, Class<T> type, AABB box, Level originalLevel, Player player, double distance) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity item)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)).collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterTeleport(Entity entity, Player player, CallbackInfo ci) {
        if (entity instanceof ItemEntity item && player.level instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) ci.cancel();
    }

    @Redirect(method = "pickupItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> magnot$filterPedestalItems(Level level, Class<T> type, AABB box, @Coerce Object pedestal, Level originalLevel, BlockPos pos) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity item)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "mob_grinding_utils.tile.TileEntityAbsorptionHopper", remap = false)
abstract class MobGrindingUtilsAbsorptionHopperMixin {
    @Redirect(method = "getCaptureItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> magnot$filterHopperItems(Level level, Class<T> type, AABB box, Predicate<? super T> predicate) {
        List<T> candidates = level.getEntitiesOfClass(type, box, predicate);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        Vec3 source = Vec3.atCenterOf(((BlockEntity) (Object) this).getBlockPos());
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity item)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", remap = false)
abstract class ModularRoutersVacuumMixin {
    @Redirect(method = "handleItemMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", remap = true), require = 0)
    private <T extends Entity> List<T> magnot$filterRouterItems(Level level, Class<T> type, AABB box, @Coerce Object router) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        Vec3 source = box.getCenter();
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity item)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", remap = false)
abstract class SophisticatedCoreMagnetMixin {
    @Unique private Vec3 magnot$source;
    @Unique private Player magnot$playerSource;

    @Inject(method = "pickupItems", at = @At("HEAD"), require = 0)
    private void magnot$captureSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$playerSource = entity instanceof Player ? (Player) entity : null;
        magnot$source = entity == null ? Vec3.atCenterOf(pos) : entity.position();
    }

    @Inject(method = "pickupItems", at = @At("RETURN"), require = 0)
    private void magnot$clearSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$source = null;
        magnot$playerSource = null;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterSophisticatedItem(Player player, ItemEntity item, CallbackInfoReturnable<Boolean> cir) {
        if (!(item.level instanceof ServerLevel serverLevel)) return;
        Player sourcePlayer = magnot$playerSource == null ? player : magnot$playerSource;
        if (sourcePlayer != null && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, sourcePlayer, item)) {
            cir.setReturnValue(false);
        } else if (sourcePlayer == null && magnot$source != null
                && FerrousMagnetRules.blocksItemPull(serverLevel, magnot$source, item)) {
            cir.setReturnValue(false);
        }
    }
}
