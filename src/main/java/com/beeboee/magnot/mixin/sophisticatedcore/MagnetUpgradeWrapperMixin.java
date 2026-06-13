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
    private boolean magnot$entityBackedMagnetSource;

    @Inject(method = "pickupItems", at = @At("HEAD"))
    private void magnot$captureMagnetSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$magnetSource = entity == null ? Vec3.atCenterOf(pos) : entity.position();
        magnot$entityBackedMagnetSource = entity != null;
    }

    @Inject(method = "pickupItems", at = @At("RETURN"))
    private void magnot$clearMagnetSource(Entity entity, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        magnot$magnetSource = null;
        magnot$entityBackedMagnetSource = false;
    }

    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true)
    private void magnot$blockFerrousRegionPickup(Player player, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!(itemEntity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (player == null && !magnot$entityBackedMagnetSource) {
            return;
        }

        Vec3 source = magnot$magnetSource;
        if (source == null) {
            source = player == null ? itemEntity.position() : player.position();
        }

        if (source.distanceToSqr(itemEntity.position()) < 1.0E-6D) {
            return;
        }

        if (FerrousMagnetRules.blocksMagnet(serverLevel, source, itemEntity.position())) {
            cir.setReturnValue(false);
        }
    }
}
