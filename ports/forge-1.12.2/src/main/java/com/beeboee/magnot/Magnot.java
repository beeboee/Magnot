package com.beeboee.magnot;

import com.beeboee.magnot.network.MagnotNetwork;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Magnot.MOD_ID,
        name = Magnot.NAME,
        version = Magnot.VERSION,
        acceptedMinecraftVersions = "[1.12.2]"
)
public final class Magnot {
    public static final String MOD_ID = "magnot";
    public static final String NAME = "Magnot";
    public static final String VERSION = "1.2.0-alpha.2+mc1.12.2";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MagnotNetwork.register();
    }
}
