package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MagnotEntityTypes {
    private static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Magnot.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<FerrousRegionEntity>> FERROUS_REGION = ENTITY_TYPES.registerEntityType(
            "ferrous_region",
            FerrousRegionEntity::new,
            MobCategory.MISC,
            builder -> builder.sized(0.1F, 0.1F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
    );

    private MagnotEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
