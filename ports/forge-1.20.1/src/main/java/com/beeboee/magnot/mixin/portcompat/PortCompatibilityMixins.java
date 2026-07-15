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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;
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

@Pseudo
@Mixin(targets = "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", remap = false)
abstract class MekanismMagneticAttractionMixin {
    @Inject(method = "pullItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterMekanismItem(Player player, ItemEntity item, CallbackInfo ci) {
        if (item.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) {
            ci.cancel();
        }
    }
}

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
abstract class DraconicEvolutionMagnetMixin {
    @Redirect(
            method = "updateMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterDraconicItems(
            Level level,
            Class<T> type,
            AABB box,
            ItemStack stack,
            Entity source
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel)
                || !(source instanceof Player player)
                || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item))
                .collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "reliquary.item.FortuneCoinItem", remap = false)
abstract class ReliquaryFortuneCoinMixin {
    @Redirect(
            method = "scanForEntitiesInRange",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterReliquaryPlayerItems(
            Level level,
            Class<T> type,
            AABB box,
            Level originalLevel,
            Player player,
            double distance
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item))
                .collect(Collectors.toList());
    }

    @Inject(method = "teleportEntityToPlayer", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$filterReliquaryTeleport(Entity entity, Player player, CallbackInfo ci) {
        if (entity instanceof ItemEntity item
                && player.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "pickupItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterReliquaryPedestalItems(
            Level level,
            Class<T> type,
            AABB box,
            @Coerce Object pedestal,
            Level originalLevel,
            BlockPos pos
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "mob_grinding_utils.tile.TileEntityAbsorptionHopper", remap = false)
abstract class MobGrindingUtilsAbsorptionHopperMixin {
    @Redirect(
            method = "getCaptureItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterAbsorptionHopperItems(
            Level level,
            Class<T> type,
            AABB box,
            Predicate<? super T> predicate
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box, predicate);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", remap = false)
abstract class ModularRoutersVacuumMixin {
    @Redirect(
            method = "handleItemMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterModularRouterItems(
            Level level,
            Class<T> type,
            AABB box,
            @Coerce Object router
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        Vec3 source = box.getCenter();
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "com.enderio.base.common.item.tool.ElectromagnetItem", remap = false)
abstract class EnderIoElectromagnetMixin {
    @Redirect(
            method = "onTickWhenActive",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private List<Entity> magnot$filterEnderIoElectromagnet(
            Level level,
            Entity excluded,
            AABB box,
            Predicate<? super Entity> predicate,
            Player player,
            ItemStack stack,
            Level originalLevel,
            Entity entity,
            int slot,
            boolean selected
    ) {
        List<Entity> candidates = level.getEntities(excluded, box, predicate);
        if (!(level instanceof ServerLevel serverLevel)) {
            return candidates;
        }
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item))
                .collect(Collectors.toList());
    }
}

@Pseudo
@Mixin(targets = "com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity", remap = false)
abstract class EnderIoVacuumMixin {
    @Redirect(
            method = "getEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterEnderIoVacuum(
            Level level,
            Class<T> type,
            AABB box,
            Predicate<? super T> predicate,
            Level originalLevel,
            BlockPos pos,
            int range,
            Predicate<T> filter
    ) {
        List<T> candidates = level.getEntitiesOfClass(type, box, predicate);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(type)) {
            return candidates;
        }
        Vec3 source = Vec3.atCenterOf(pos);
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .collect(Collectors.toList());
    }
}
