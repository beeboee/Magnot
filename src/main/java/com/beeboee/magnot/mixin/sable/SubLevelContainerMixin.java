package com.beeboee.magnot.mixin.sable;

import com.beeboee.magnot.compat.sable.MagnotSableRegionExit;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.ryanhcode.sable.api.sublevel.SubLevelContainer", remap = false)
public abstract class SubLevelContainerMixin {
    @Shadow
    @Final
    private Level level;

    @Shadow
    public abstract SubLevel getSubLevel(int x, int z);

    @Inject(method = "removeSubLevel(IILdev/ryanhcode/sable/sublevel/storage/SubLevelRemovalReason;)V", at = @At("HEAD"), require = 0)
    private void magnot$handleRegionExit(int x, int z, SubLevelRemovalReason reason, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        SubLevel subLevel = getSubLevel(x, z);
        if (subLevel != null) {
            MagnotSableRegionExit.handle(serverLevel, subLevel);
        }
    }
}
