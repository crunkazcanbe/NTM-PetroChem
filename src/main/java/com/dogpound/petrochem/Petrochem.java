package com.dogpound.petrochem;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Petrochem.MODID, name = "NTM Petrochem", version = "0.1.0", acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:hbm")
public class Petrochem {
    public static final String MODID = "petrochem";
    @Mod.Instance(MODID)
    public static Petrochem instance;
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Pre-initializing NTM Petrochem");
        MinecraftForge.EVENT_BUS.register(this);
        PetrochemFluids.init();
        PetrochemRegistry.registerTiles();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Initializing NTM Petrochem");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Post-initializing NTM Petrochem");
    }
}
