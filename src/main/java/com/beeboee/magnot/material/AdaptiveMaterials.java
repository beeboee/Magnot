package com.beeboee.magnot.material;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Collection;

/**
 * One authoritative view of the optional material state used by recipes, tabs,
 * and recipe viewers.
 */
public final class AdaptiveMaterials {
    public static final TagKey<Item> IRON_DUSTS = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath("c", "dusts/iron")
    );
    public static final TagKey<Item> IRON_PLATES = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath("c", "plates/iron")
    );
    public static final ResourceLocation MAGNOT_IRON_DUST = ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "iron_dust");

    private AdaptiveMaterials() {
    }

    public static boolean externalIronDustAvailable(ICondition.IContext context) {
        return externalIronDustAvailable(context.getTag(IRON_DUSTS));
    }

    public static boolean externalIronDustAvailable(Collection<? extends Holder<Item>> holders) {
        return externalIronDustAvailableIds(holders.stream()
                .map(holder -> holder.unwrapKey()
                        .map(key -> key.location())
                        .orElseGet(() -> BuiltInRegistries.ITEM.getKey(holder.value())))
                .toList());
    }

    static boolean externalIronDustAvailableIds(Collection<ResourceLocation> ids) {
        return ids.stream().anyMatch(id -> !MAGNOT_IRON_DUST.equals(id));
    }

    public static boolean ironPlateAvailable(ICondition.IContext context) {
        return !context.getTag(IRON_PLATES).isEmpty();
    }

    public static boolean externalIronDustAvailable() {
        return BuiltInRegistries.ITEM.getTag(IRON_DUSTS)
                .map(named -> externalIronDustAvailable(named.stream().toList()))
                .orElse(false);
    }

    public static boolean ironPlateAvailable() {
        return BuiltInRegistries.ITEM.getTag(IRON_PLATES).map(named -> named.size() > 0).orElse(false);
    }

    public static boolean fallbackDustRequired() {
        return ModList.get().isLoaded("create") && !externalIronDustAvailable();
    }

    public static boolean magnotIronDustDormant() {
        return !fallbackDustRequired();
    }

    public static Item fallbackDustItem() {
        return MagnotItems.IRON_DUST.get();
    }

    public enum MaterialState {
        EXTERNAL_IRON_DUST("external_iron_dust"),
        IRON_PLATE("iron_plate");

        private final String serializedName;

        MaterialState(String serializedName) {
            this.serializedName = serializedName;
        }

        public String serializedName() {
            return serializedName;
        }

        public boolean test(ICondition.IContext context) {
            return switch (this) {
                case EXTERNAL_IRON_DUST -> externalIronDustAvailable(context);
                case IRON_PLATE -> ironPlateAvailable(context);
            };
        }

        public static MaterialState byName(String name) {
            for (MaterialState state : values()) {
                if (state.serializedName.equals(name)) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Unknown Magnot material state: " + name);
        }
    }
}
