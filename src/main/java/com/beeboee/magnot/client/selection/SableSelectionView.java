package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.Optional;

public interface SableSelectionView {
    Optional<SelectionHit> closestIntersecting(Level level, Vec3 from, Vec3 to);

    FerrousSelectionView view(Level level, FerrousRegion region);

    static SableSelectionView load() {
        if (ModList.get().isLoaded("sable")) {
            try {
                return (SableSelectionView) Class.forName("com.beeboee.magnot.compat.sable.client.SableSelectionViewImpl")
                        .getDeclaredConstructor()
                        .newInstance();
            } catch (ReflectiveOperationException | LinkageError error) {
                Magnot.LOGGER.error("Could not initialize Sable client selection support", error);
            }
        }
        return None.INSTANCE;
    }

    record SelectionHit(FerrousRegion region, double distanceSqr) {
    }

    enum None implements SableSelectionView {
        INSTANCE;

        @Override
        public Optional<SelectionHit> closestIntersecting(Level level, Vec3 from, Vec3 to) {
            return Optional.empty();
        }

        @Override
        public FerrousSelectionView view(Level level, FerrousRegion region) {
            return FerrousSelectionView.axisAligned(region.bounds());
        }
    }
}
