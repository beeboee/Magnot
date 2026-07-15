package com.beeboee.magnot.compat.emi;

import com.beeboee.magnot.material.AdaptiveMaterials;
import com.beeboee.magnot.registry.MagnotItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

/** Optional EMI integration. This class is only discovered when EMI is installed. */
@EmiEntrypoint
public final class MagnotEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        if (AdaptiveMaterials.magnotIronDustDormant()) {
            registry.removeEmiStacks(EmiStack.of(MagnotItems.IRON_DUST.get()));
        }
    }
}
