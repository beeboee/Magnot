package com.beeboee.magnot.compat.create.sable;

import com.beeboee.magnot.compat.create.client.MagnotCreateTexture;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.neoforge.mixinhelper.compatibility.create.renderers.AABBOutlineRenderingOptions;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.world.level.Level;

public final class CreateSableOutlineBridgeImpl implements CreateSableOutlineBridge {
    @Override
    public boolean show(Level level, Object slot, FerrousRegion region, int color, boolean textured, float lineWidth) {
        if (region.isWorldRegion() || !hasSubLevel(level, region)) {
            disableTransform(slot);
            return false;
        }

        var texture = textured ? MagnotCreateTexture.FERROUS_REGION : null;
        Outliner.getInstance().showAABB(slot, region.bounds())
                .colored(color)
                .withFaceTextures(texture, texture)
                .disableLineNormals()
                .lineWidth(lineWidth);

        var entry = Outliner.getInstance().getOutlines().get(slot);
        if (entry != null && entry.getOutline() instanceof AABBOutlineRenderingOptions options) {
            options.sable$shouldTransform(true);
        }
        return true;
    }

    private static boolean hasSubLevel(Level level, FerrousRegion region) {
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return false;
        }
        for (SubLevelAccess subLevel : container.getAllSubLevels()) {
            if (region.belongsToSubLevel(subLevel.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private static void disableTransform(Object slot) {
        var entry = Outliner.getInstance().getOutlines().get(slot);
        if (entry != null && entry.getOutline() instanceof AABBOutlineRenderingOptions options) {
            options.sable$shouldTransform(false);
        }
    }
}
