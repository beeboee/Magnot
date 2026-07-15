package com.beeboee.magnot.compat.jei;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.material.AdaptiveMaterials;
import com.beeboee.magnot.registry.MagnotItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** Optional JEI integration. This class is only discovered when JEI is installed. */
@JeiPlugin
public final class MagnotJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "adaptive_materials");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime runtime) {
        if (AdaptiveMaterials.magnotIronDustDormant()) {
            runtime.getIngredientManager().removeIngredientsAtRuntime(
                    VanillaTypes.ITEM_STACK,
                    List.of(new ItemStack(MagnotItems.IRON_DUST.get()))
            );
        }
    }
}
