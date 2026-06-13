package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MagnotEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Magnot.MOD_ID);

    public static final Supplier<EntityType<FerrousRegionEntity>> FERROUS_REGION = ENTITY_TYPES.register(
            "ferrous_region",
            () -> EntityType.Builder.<FerrousRegionEntity>of(
                            (EntityType<FerrousRegionEntity> type, Level level) -> new FerrousRegionEntity(type, level),
                            MobCategory.MISC
                    )
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("ferrous_region")
    );

    private MagnotEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
