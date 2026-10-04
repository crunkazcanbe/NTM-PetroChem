package com.dogpound.petrochem.fluids;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.render.misc.EnumSymbol;
import java.lang.reflect.Field;
import java.util.List;

public class PetroFluidType
extends FluidType {
    public static int id = 0;
    private static List<FluidType> metaOrder;

    static {
        try {
            Field field = Fluids.class.getDeclaredField("metaOrder");
            field.setAccessible(true);
            metaOrder = (List<FluidType>) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PetroFluidType(String name, int color, int p, int f, int r, EnumSymbol symbol) {
        super(name, color, p, f, r, symbol, name.toLowerCase(java.util.Locale.US), 0xFFFFFF, 1400 + id++, null);
        metaOrder.add(this);
    }
}
