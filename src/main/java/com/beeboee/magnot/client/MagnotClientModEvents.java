package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.registry.MagnotEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class MagnotClientModEvents {
    private MagnotClientModEvents() {
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MagnotEntityTypes.FERROUS_REGION.get(), FerrousRegionEntityRenderer::new);
    }
}
