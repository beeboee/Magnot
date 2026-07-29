package com.beeboee.magnot.proxy;

import com.beeboee.magnot.client.ClientRegionStore;
import com.beeboee.magnot.client.MagnotClientEvents;
import com.beeboee.magnot.region.FerrousRegion;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public final class ClientProxy extends CommonProxy {
    @Override
    public void registerClientEvents() {
        MagnotClientEvents events = new MagnotClientEvents();
        MinecraftForge.EVENT_BUS.register(events);
        FMLCommonHandler.instance().bus().register(events);
    }

    @Override
    public void acceptRegionSync(List<FerrousRegion> regions) {
        ClientRegionStore.set(regions);
    }
}
