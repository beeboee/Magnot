package com.beeboee.magnot.condition;

import com.beeboee.magnot.material.AdaptiveMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record MaterialAvailableCondition(AdaptiveMaterials.MaterialState material, boolean expected) implements ICondition {
    private static final Codec<AdaptiveMaterials.MaterialState> MATERIAL_CODEC = Codec.STRING.xmap(
            AdaptiveMaterials.MaterialState::byName,
            AdaptiveMaterials.MaterialState::serializedName
    );

    public static final MapCodec<MaterialAvailableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(MaterialAvailableCondition::material),
            Codec.BOOL.optionalFieldOf("expected", true).forGetter(MaterialAvailableCondition::expected)
    ).apply(instance, MaterialAvailableCondition::new));

    @Override
    public boolean test(IContext context) {
        return material.test(context) == expected;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
