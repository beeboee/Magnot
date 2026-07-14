package com.beeboee.magnot;

import com.beeboee.magnot.registry.MagnotItems;
import com.beeboee.magnot.server.MagnotForgeEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Magnot.MOD_ID)
public final class Magnot {
    public static final String MOD_ID = "magnot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Magnot() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MagnotItems.register(modBus);
        MinecraftForge.EVENT_BUS.register(MagnotForgeEvents.class);
    }
}
