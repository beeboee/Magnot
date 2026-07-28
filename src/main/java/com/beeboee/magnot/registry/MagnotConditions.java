package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.condition.MaterialAvailableCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class MagnotConditions {
    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(
            NeoForgeRegistries.CONDITION_SERIALIZERS,
            Magnot.MOD_ID
    );

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<MaterialAvailableCondition>> MATERIAL_AVAILABLE =
            CONDITIONS.register("material_available", () -> MaterialAvailableCondition.CODEC);

    private MagnotConditions() {
    }

    public static void register(IEventBus eventBus) {
        CONDITIONS.register(eventBus);
    }
}
