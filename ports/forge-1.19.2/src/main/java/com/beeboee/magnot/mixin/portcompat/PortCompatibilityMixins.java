package com.beeboee.magnot.mixin.portcompat;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    @Redirect(method = "handleMagnet(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V"), require = 0)
    private static void magnot$filterAe2wtlibPickup(ItemEntity item, Player player) {
        if (item.level instanceof ServerLevel && FerrousMagnetRules.blocksPlayerItemPull((ServerLevel) item.level, player, item)) return;
        item.playerTouch(player);
    }
}

@Pseudo
@Mixin(targets = "artifacts.effect.MagnetismMobEffect", remap = false)
abstract class ArtifactsMagnetismMixin {
    @Redirect(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private List<ItemEntity> magnot$filterArtifactsItems(Level level, Class<ItemEntity> type, AABB box, LivingEntity source, int amplifier) {
        List<ItemEntity> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 sourcePos = source.position().add(0.0D, 0.75D, 0.0D);
        return candidates.stream().filter(item -> source instanceof Player
                ? !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, (Player) source, item)
                : !FerrousMagnetRules.blocksItemPull(serverLevel, sourcePos, item)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", remap = false)
abstract class MekanismMagneticAttractionMixin {
    @Inject(method = "pullItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterMekanismItem(Player player, ItemEntity item, CallbackInfo ci) {
        if (item.level instanceof ServerLevel && FerrousMagnetRules.blocksPlayerItemPull((ServerLevel) item.level, player, item)) ci.cancel();
    }
}

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
abstract class DraconicEvolutionMagnetMixin {
    @Redirect(method = "updateMagnet", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterDraconicItems(Level level, Class<T> type, AABB box, ItemStack stack, Entity source) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !(source instanceof Player) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Player player = (Player) source;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "reliquary.item.FortuneCoinItem", remap = false)
abstract class ReliquaryFortuneCoinMixin {
    @Redirect(method = "scanForEntitiesInRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterReliquaryPlayerItems(Level level, Class<T> type, AABB box, Level originalLevel, Player player, double distance) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, (ItemEntity) candidate)).collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterReliquaryTeleport(Entity entity, Player player, CallbackInfo ci) {
        if (entity instanceof ItemEntity && player.level instanceof ServerLevel
                && FerrousMagnetRules.blocksPlayerItemPull((ServerLevel) player.level, player, (ItemEntity) entity)) ci.cancel();
    }

    @Redirect(method = "pickupItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterReliquaryPedestalItems(Level level, Class<T> type, AABB box, @Coerce Object pedestal, Level originalLevel, BlockPos pos) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "mob_grinding_utils.tile.TileEntityAbsorptionHopper", remap = false)
abstract class MobGrindingUtilsAbsorptionHopperMixin {
    @Redirect(method = "getCaptureItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterAbsorptionHopperItems(Level level, Class<T> type, AABB box, Predicate<? super T> predicate) {
        List<T> candidates = level.getEntitiesOfClass(type, box, predicate);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 source = Vec3.atCenterOf(((BlockEntity) (Object) this).getBlockPos());
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", remap = false)
abstract class ModularRoutersVacuumMixin {
    @Redirect(method = "handleItemMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filterModularRouterItems(Level level, Class<T> type, AABB box, @Coerce Object router) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 source = box.getCenter();
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity)
                || !FerrousMagnetRules.blocksItemPull(serverLevel, source, (ItemEntity) candidate)).collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", remap = false)
abstract class SophisticatedCoreMagnetMixin {
    @Unique private Vec3 magnot$source;
    @Unique private Player magnot$playerSource;

    @Inject(method = "pickupItems", at = @At("HEAD"), require = 0)
    private void magnot$captureSophisticatedSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$playerSource = entity instanceof Player ? (Player) entity : null;
        magnot$source = entity == null ? Vec3.atCenterOf(pos) : entity.position();
    }

    @Inject(method = "pickupItems", at = @At("RETURN"), require = 0)
    private void magnot$clearSophisticatedSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$source = null;
        magnot$playerSource = null;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterSophisticatedItem(Player player, ItemEntity item, CallbackInfoReturnable<Boolean> cir) {
        if (!(item.level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel) item.level;
        Player sourcePlayer = magnot$playerSource == null ? player : magnot$playerSource;
        if (sourcePlayer != null && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, sourcePlayer, item)) {
            cir.setReturnValue(false);
        } else if (sourcePlayer == null && magnot$source != null && FerrousMagnetRules.blocksItemPull(serverLevel, magnot$source, item)) {
            cir.setReturnValue(false);
        }
    }
}
