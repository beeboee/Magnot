package com.beeboee.magnot.compat.create.sable;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

public interface CreateSableOutlineBridge {
    boolean show(Level level, Object slot, FerrousRegion region, int color, boolean textured, float lineWidth);

    static CreateSableOutlineBridge load() {
        if (ModList.get().isLoaded("sable")) {
            try {
                return (CreateSableOutlineBridge) Class.forName("com.beeboee.magnot.compat.create.sable.CreateSableOutlineBridgeImpl")
                        .getDeclaredConstructor()
                        .newInstance();
            } catch (ReflectiveOperationException | LinkageError error) {
                Magnot.LOGGER.error("Could not initialize Create/Sable outline bridge", error);
            }
        }
        return (level, slot, region, color, textured, lineWidth) -> false;
    }
}
