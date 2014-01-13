package zettabyte.weirdscience.tileentity;

import zettabyte.weirdscience.WeirdScience;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileEntityBloodDonation extends TileFluidHandler {

	public TileEntityBloodDonation() {
		super();
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}
	public Packet getDescriptionPacket() {
	    NBTTagCompound nbt = new NBTTagCompound();
	    writeToNBT(nbt);
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	private Fluid bloodFluid;
	
	public void setFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	public void setStorageCap(int setMax) {
		tank.setCapacity(setMax);
	}
	
	@Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
		if(resource.getFluid().equals(bloodFluid)) {
	        return tank.fill(resource, doFill);
		}
		else {
			return 0;
		}
    }

}
