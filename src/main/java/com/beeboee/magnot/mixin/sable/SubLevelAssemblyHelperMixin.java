package com.beeboee.magnot.mixin.sable;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.StreamSupport;

@Pseudo
@Mixin(targets = "dev.ryanhcode.sable.api.SubLevelAssemblyHelper", remap = false)
public abstract class SubLevelAssemblyHelperMixin {
    @Redirect(
            method = "assembleBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/ryanhcode/sable/api/SubLevelAssemblyHelper;moveBlocks(Lnet/minecraft/server/level/ServerLevel;Ldev/ryanhcode/sable/api/SubLevelAssemblyHelper$AssemblyTransform;Ljava/lang/Iterable;)V"
            )
    )
    private static void magnot$moveFerrousRegionsWithBlocks(ServerLevel level, SubLevelAssemblyHelper.AssemblyTransform transform, Iterable<BlockPos> blocks) {
        var copiedBlocks = StreamSupport.stream(blocks.spliterator(), false)
                .map(BlockPos::immutable)
                .toList();

        SubLevelAssemblyHelper.moveBlocks(level, transform, copiedBlocks);
        MagnotSableCompat.moveRegionsAfterAssembly(level, transform, copiedBlocks);
    }
}
