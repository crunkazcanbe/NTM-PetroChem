package com.dogpound.petrochem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PetrochemCreativeTab extends CreativeTabs {
    public static final PetrochemCreativeTab PETROCHEM_TAB = new PetrochemCreativeTab();
    
    private PetrochemCreativeTab() {
        super("petrochem");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
        return new ItemStack(Items.BUCKET);
    }
}
