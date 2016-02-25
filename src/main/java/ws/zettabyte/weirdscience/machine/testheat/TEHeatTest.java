package ws.zettabyte.weirdscience.machine.testheat;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import ws.zettabyte.zettalib.block.TileEntityBase;
import ws.zettabyte.zettalib.inventory.IComponentContainer;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.ItemSlot;
import ws.zettabyte.zettalib.thermal.HeatRegistry;
import ws.zettabyte.zettalib.thermal.IHasHeatLogic;
import ws.zettabyte.zettalib.thermal.IHeatLogic;
import ws.zettabyte.zettalib.thermal.SimpleHeatLogic;

public class TEHeatTest extends TileEntityBase implements IHasHeatLogic,
		IComponentContainer, IDescriptiveInventory {

	protected ArrayList<IInvComponent> fullComponentList = new ArrayList<IInvComponent>(); //new ArrayList<IInvComponent>(8);
	protected SimpleHeatLogic tempLogic = new SimpleHeatLogic();
	public TEHeatTest() {
		tempLogic.te = this;
		fullComponentList.add(tempLogic);
    	trySetupHeat();
	}
	@Override
	public Iterable<IInvComponent> getComponents() {
		return fullComponentList;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	@Override
	public IHeatLogic getHeatLogic() {
		//tempLogic.setupAmbientHeat(worldObj, xCoord, yCoord, zCoord);
		return tempLogic;
	}
	//---- NBT stuff ----
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        tempLogic.readFromNBT(nbt);
        tempLogic.initialized = true;
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        tempLogic.writeToNBT(nbt);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}
	
	//------------- Update behavior -----------
	protected void trySetupHeat() {
    	if((worldObj != null) && !tempLogic.initialized){
    		//if(worldObj.isRemote) return;
    		tempLogic.setupAmbientHeat(worldObj, xCoord, yCoord, zCoord);
    		this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}
	}

    @Override
    public void updateEntity () {
    	super.updateEntity();
    	trySetupHeat();
    	
    	tempLogic.process();
    	ArrayList<IHeatLogic> testAgainst = new ArrayList<IHeatLogic>(3);
    	for(EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
    		TileEntity t = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
    		if(t != null) {
    			if(t instanceof IHeatLogic) {
    				testAgainst.add((IHeatLogic)t);
    			}
    		}
    	}

    	if(testAgainst.size() != 0 ) {
    		this.tempLogic.doBalance(testAgainst);
    	}
    	if((worldObj.getTotalWorldTime() % tempLogic.getTicksPerPassiveLoss()) == 0) {
    		this.tempLogic.doPassiveLoss();
    	}
		//Try in-world heat stuff
		EnumFacing check;
		for(int i = 0; i < 6; ++i) {
			check = EnumFacing.VALID_DIRECTIONS[i];
			//Block b = worldObj.getBlock(xCoord + check.offsetX,
			//        yCoord + check.offsetY,
			//        zCoord + check.offsetZ);
			HeatRegistry.getInstance().TryBlockBehaviorAt(worldObj, xCoord + check.offsetX,
					yCoord + check.offsetY,
					zCoord + check.offsetZ,
					this.tempLogic);
		}
    	
    	if(tempLogic.isDirty()) {
    		this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}
    }
	
	
	@Override
	public int getSizeInventory() { return 0; }
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) { return null; }
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) { return null; }
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) { return null; }
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {}
	@Override
	public String getInventoryName() { return null; }
	@Override
	public boolean hasCustomInventoryName() { return false; }
	@Override
	public int getInventoryStackLimit() { return 0; }
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) { return true; }
	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) { return false; }
	@Override
	public Iterable<ItemSlot> getSlots() { return null; }
}
