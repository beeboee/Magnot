package com.beeboee.magnot.mixin.mekanism;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", remap = false)
public abstract class ModuleMagneticAttractionUnitMixin {
    @Inject(method = "pullItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void magnot$blockFerrousRegionPull(Player player, ItemEntity item, CallbackInfo ci) {
        if (item.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, item.position())) {
            ci.cancel();
        }
    }
}
