package com.beeboee.magnot.compat.create.client;

import com.beeboee.magnot.client.selection.FerrousSelectionBackend;
import com.beeboee.magnot.client.selection.FerrousSelectionView;
import com.beeboee.magnot.compat.create.sable.CreateSableOutlineBridge;
import com.beeboee.magnot.region.FerrousRegion;
import com.simibubi.create.foundation.utility.RaycastHelper;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Reuses Create's raycasting and Catnip Outliner without creating Create glue entities or packets. */
public final class CreateFerrousSelectionBackend implements FerrousSelectionBackend {
    private final CreateSableOutlineBridge sable = CreateSableOutlineBridge.load();

    @Override
    public String name() {
        return "create";
    }

    @Override
    public void beginFrame() {
        // Outliner entries fade naturally unless refreshed by showOutline.
    }

    @Override
    public void showOutline(Level level, Object slot, FerrousRegion region, FerrousSelectionView view, int color, boolean textured, float lineWidth) {
        if (sable.show(level, slot, region, color, textured, lineWidth)) {
            return;
        }
        var texture = textured ? MagnotCreateTexture.FERROUS_REGION : null;
        Outliner.getInstance().showAABB(slot, view.displayBounds())
                .colored(color)
                .withFaceTextures(texture, texture)
                .disableLineNormals()
                .lineWidth(lineWidth);
    }

    @Override
    public Vec3 traceTarget(LocalPlayer player, double range, Vec3 origin) {
        return RaycastHelper.getTraceTarget(player, range, origin);
    }
}
