package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public interface FerrousSelectionBackend {
    String name();

    void beginFrame();

    void showOutline(Level level, Object slot, FerrousRegion region, FerrousSelectionView view, int color, boolean textured, float lineWidth);

    default Vec3 traceTarget(LocalPlayer player, double range, Vec3 origin) {
        return origin.add(player.getLookAngle().scale(range));
    }

    default void render(RenderLevelStageEvent event) {
    }
}
