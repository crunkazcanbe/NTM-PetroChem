package com.dogpound.petrochem;

import com.dogpound.petrochem.fluids.PetroFluidType;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.hbm.render.misc.EnumSymbol;

public class PetrochemFluids {
    public static FluidType ETHYLENE;
    public static FluidType PROPYLENE;

    public static void init() {
        ETHYLENE = new PetroFluidType("ETHYLENE", 0xB6E2A1, 0, 3, 0, EnumSymbol.NONE)
                .setTemp(20)
                .addTraits(new FluidTrait[]{ Fluids.GASEOUS });
        PROPYLENE = new PetroFluidType("PROPYLENE", 0xC8B6E2, 1, 3, 0, EnumSymbol.NONE)
                .setTemp(20)
                .addTraits(new FluidTrait[]{ Fluids.GASEOUS });
    }
}
