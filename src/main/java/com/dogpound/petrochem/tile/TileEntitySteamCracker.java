package com.dogpound.petrochem.tile;

import com.dogpound.petrochem.PetrochemFluids;
import com.hbm.api.fluid.IFluidStandardTransceiver;
import com.hbm.capability.NTMFluidHandlerWrapper;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.IConnectionAnchors;
import com.hbm.tileentity.TileEntityLoadedBase;
import com.hbm.util.Tuple;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamCracker extends TileEntityLoadedBase implements ITickable, IFluidStandardTransceiver, IConnectionAnchors {
    public FluidTankNTM[] tanks = new FluidTankNTM[5];
    AxisAlignedBB bb = null;

    public TileEntitySteamCracker() {
        this.tanks[0] = new FluidTankNTM(Fluids.NAPHTHA, 4000).withOwner(this);
        this.tanks[1] = new FluidTankNTM(Fluids.STEAM, 8000).withOwner(this);
        this.tanks[2] = new FluidTankNTM(PetrochemFluids.ETHYLENE, 4000).withOwner(this);
        this.tanks[3] = new FluidTankNTM(PetrochemFluids.PROPYLENE, 4000).withOwner(this);
        this.tanks[4] = new FluidTankNTM(Fluids.SPENTSTEAM, 4000).withOwner(this);
    }

    public void update() {
        if (!this.world.isRemote) {
            setupTanks();
            updateConnections();
            if (this.world.getTotalWorldTime() % 5L == 0L) {
                crack();
            }
            if (this.world.getTotalWorldTime() % 10L == 0L) {
                for (DirPos pos : this.getConPos()) {
                    for (int i = 2; i <= 4; ++i) {
                        if (this.tanks[i].getFill() <= 0) continue;
                        this.sendFluid(this.tanks[i], this.world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
                    }
                }
                this.networkPackNT(50);
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        for (int i = 0; i < 5; ++i) {
            this.tanks[i].serialize(buf);
        }
    }

    @Override
    public void deserialize(ByteBuf buf) {
        for (int i = 0; i < 5; ++i) {
            this.tanks[i].deserialize(buf);
        }
    }

    private void updateConnections() {
        for (DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tanks[0].getTankType(), this.world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
            this.trySubscribe(this.tanks[1].getTankType(), this.world, pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ(), pos.getDir());
        }
    }

    @Override
    public DirPos[] getConPos() {
        return new DirPos[]{
            new DirPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ(), ForgeDirection.EAST),
            new DirPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ(), ForgeDirection.WEST),
            new DirPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1, ForgeDirection.SOUTH),
            new DirPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1, ForgeDirection.NORTH),
            new DirPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), ForgeDirection.UP),
            new DirPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ(), ForgeDirection.DOWN)
        };
    }

    private void setupTanks() {
        this.tanks[0].setTankType(Fluids.NAPHTHA);
        this.tanks[1].setTankType(Fluids.STEAM);
        this.tanks[2].setTankType(PetrochemFluids.ETHYLENE);
        this.tanks[3].setTankType(PetrochemFluids.PROPYLENE);
        this.tanks[4].setTankType(Fluids.SPENTSTEAM);
    }

    private void crack() {
        for (int i = 0; i < 2; ++i) {
            if (this.tanks[0].getFill() >= 100 && this.tanks[1].getFill() >= 200 && hasSpace(80, 40)) {
                this.tanks[0].setFill(this.tanks[0].getFill() - 100);
                this.tanks[1].setFill(this.tanks[1].getFill() - 200);
                this.tanks[2].setFill(this.tanks[2].getFill() + 80);
                this.tanks[3].setFill(this.tanks[3].getFill() + 40);
                this.tanks[4].setFill(this.tanks[4].getFill() + 2);
            }
        }
    }

    private boolean hasSpace(int left, int right) {
        return this.tanks[2].getFill() + left <= this.tanks[2].getMaxFill() && this.tanks[3].getFill() + right <= this.tanks[3].getMaxFill() && this.tanks[4].getFill() + 2 <= this.tanks[4].getMaxFill();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for (int i = 0; i < this.tanks.length; ++i) {
            this.tanks[i].readFromNBT(nbt, "tank" + i);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        for (int i = 0; i < this.tanks.length; ++i) {
            this.tanks[i].writeToNBT(nbt, "tank" + i);
        }
        return nbt;
    }

    public AxisAlignedBB getRenderBoundingBox() {
        if (this.bb == null) {
            this.bb = new AxisAlignedBB((double)(this.pos.getX() - 3), (double)this.pos.getY(), (double)(this.pos.getZ() - 3), (double)(this.pos.getX() + 4), (double)(this.pos.getY() + 16), (double)(this.pos.getZ() + 4));
        }
        return this.bb;
    }

    @SideOnly(value=Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @Override
    public FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[]{this.tanks[2], this.tanks[3], this.tanks[4]};
    }

    @Override
    public FluidTankNTM[] getReceivingTanks() {
        return new FluidTankNTM[]{this.tanks[0], this.tanks[1]};
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return this.tanks;
    }

    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T)CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new NTMFluidHandlerWrapper(this));
        }
        return (T)super.getCapability(capability, facing);
    }
}
