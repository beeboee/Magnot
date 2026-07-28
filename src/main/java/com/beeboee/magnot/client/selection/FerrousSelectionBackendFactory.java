package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.Magnot;
import net.neoforged.fml.ModList;

public final class FerrousSelectionBackendFactory {
    private FerrousSelectionBackendFactory() {
    }

    public static FerrousSelectionBackend create() {
        if (ModList.get().isLoaded("create")) {
            try {
                return (FerrousSelectionBackend) Class.forName("com.beeboee.magnot.compat.create.client.CreateFerrousSelectionBackend")
                        .getDeclaredConstructor()
                        .newInstance();
            } catch (ReflectiveOperationException | LinkageError error) {
                Magnot.LOGGER.error("Could not initialize Create-backed ferrous selection; using native rendering", error);
            }
        }
        return new NativeFerrousSelectionBackend();
    }
}
