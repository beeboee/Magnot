package com.beeboee.magnot.mixin.sophisticatedcore;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", remap = false)
public abstract class MagnetUpgradeWrapperMixin {
    @Unique
    private Vec3 magnot$source;
    @Unique
    private Player magnot$playerSource;

    @Inject(method = "pickupItems", at = @At("HEAD"), require = 0)
    private void magnot$captureSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$playerSource = entity instanceof Player player ? player : null;
        magnot$source = entity == null ? Vec3.atCenterOf(pos) : entity.position();
    }

    @Inject(method = "pickupItems", at = @At("RETURN"), require = 0)
    private void magnot$clearSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$source = null;
        magnot$playerSource = null;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$blockPickup(Player player, ItemEntity item, CallbackInfoReturnable<Boolean> cir) {
        if (!(item.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Player sourcePlayer = magnot$playerSource == null ? player : magnot$playerSource;
        if (sourcePlayer != null && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, sourcePlayer, item)) {
            cir.setReturnValue(false);
        } else if (sourcePlayer == null && magnot$source != null
                && FerrousMagnetRules.blocksItemPull(serverLevel, magnot$source, item)) {
            cir.setReturnValue(false);
        }
    }
}
