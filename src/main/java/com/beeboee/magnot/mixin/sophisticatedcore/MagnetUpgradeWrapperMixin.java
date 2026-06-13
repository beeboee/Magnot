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
    private Vec3 magnot$magnetSource;

    @Unique
    private Player magnot$playerMagnetSource;

    @Inject(method = "pickupItems", at = @At("HEAD"))
    private void magnot$captureMagnetSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$playerMagnetSource = entity instanceof Player player ? player : null;
        magnot$magnetSource = entity == null ? Vec3.atCenterOf(pos) : entity.position();
    }

    @Inject(method = "pickupItems", at = @At("RETURN"))
    private void magnot$clearMagnetSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$magnetSource = null;
        magnot$playerMagnetSource = null;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true)
    private void magnot$blockFerrousRegionPickup(Player player, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!(itemEntity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Player playerSource = magnot$playerMagnetSource == null ? player : magnot$playerMagnetSource;
        if (playerSource != null) {
            if (FerrousMagnetRules.blocksPlayerMagnet(serverLevel, playerSource, itemEntity.position())) {
                cir.setReturnValue(false);
            }
            return;
        }

        Vec3 source = magnot$magnetSource == null ? itemEntity.position() : magnot$magnetSource;
        if (FerrousMagnetRules.blocksMagnet(serverLevel, source, itemEntity.position())) {
            cir.setReturnValue(false);
        }
    }
}
