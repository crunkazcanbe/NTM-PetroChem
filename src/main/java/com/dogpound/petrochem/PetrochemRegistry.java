package com.dogpound.petrochem;
import com.dogpound.petrochem.block.BlockSteamCracker;
import com.dogpound.petrochem.tile.TileEntitySteamCracker;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Petrochem.MODID)
public class PetrochemRegistry {
    
    public static final BlockSteamCracker STEAM_CRACKER = new BlockSteamCracker();
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(STEAM_CRACKER);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        ItemBlock ib = new ItemBlock(STEAM_CRACKER);
        ib.setRegistryName(STEAM_CRACKER.getRegistryName());
        e.getRegistry().register(ib);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent e) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(STEAM_CRACKER), 0, new ModelResourceLocation(STEAM_CRACKER.getRegistryName(), "inventory"));
    }
    
    public static void registerTiles() {
        GameRegistry.registerTileEntity(TileEntitySteamCracker.class, new ResourceLocation(Petrochem.MODID, "steam_cracker"));
    }
}
