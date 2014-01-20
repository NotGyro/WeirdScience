package zettabyte.weirdscience.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.WeirdScience;
import zettabyte.weirdscience.tileentity.TileEntityBloodDonation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBloodDonation extends BlockContainer {
	
	private static Fluid bloodFluid;
	private static int mbPerDonation;
	private static int dmgPerDonation;
	private static int maxStorage;
	
	public BlockBloodDonation(int ID, Material Mat) {
		super(ID, Mat);
        this.setCreativeTab(WeirdScience.tabWeirdScience);
    }

	public static void setFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	public static void setStorageCap(int setMax) {
		maxStorage = setMax;
	}
	public static void setDamagePer(int setDmg) {
		dmgPerDonation = setDmg;
	}
	public static void setDonationAmt(int setAmt) {
		mbPerDonation = setAmt;
	}

    @SideOnly(Side.CLIENT)
    protected Icon iconTop;

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int par2)
    {
        if(side == 1) { //Top
        	return this.iconTop;
        }
        else {
        	return this.blockIcon;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("weirdscience:genericmachine");
        this.iconTop = iconRegister.registerIcon("weirdscience:blooddonationtop");
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityBloodDonation TE = new TileEntityBloodDonation();
		TE.setBloodFluid(bloodFluid);
		TE.setStorageCap(maxStorage);
		return TE;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		TileEntityBloodDonation donationEntity = (TileEntityBloodDonation)tileEntity;
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		ItemStack playerItem = player.inventory.getCurrentItem();

		//Check to see if the player is holding something that can be filled with a fluid.
		if (playerItem != null) {
			//Make sure there is fluid to take.
			FluidStack available = donationEntity.getFluid();
			if (available != null) {
				//Check to see if we can fill this item.
				ItemStack filledItem = FluidContainerRegistry.fillFluidContainer(available, playerItem);
				if(filledItem != null) {
					//Get the volume of our container.
					FluidStack toDrain = FluidContainerRegistry.getFluidForFilledItem(filledItem);
					if (toDrain != null) {
						//Do we have more than one bucket/canister/whatever?
						if (playerItem.stackSize > 1) {
							//If so, try to add the item to the player's inventory
							if (!player.inventory.addItemStackToInventory(filledItem)) {
								return false;
							}
							//Decrement our stack size.
							--playerItem.stackSize;
							//TODO: Check to see if this is actually necessary.
							player.inventory.setInventorySlotContents(player.inventory.currentItem, playerItem);
						}
						else {
							//Set the slot to our filled item.
							player.inventory.setInventorySlotContents(player.inventory.currentItem, filledItem);
							playerItem = null;
						}
						//Remove the blood from our tile entity.
						donationEntity.drain(toDrain.amount, true);
					}
				}
			}
			return true;
		}
		else {
			//The player is not holding a bucket.
			//Try to harm the player. Bucketlessness is a sin.
		    float previousPlayerHealth = player.getHealth();
			player.attackEntityFrom(DamageSource.magic, (float)dmgPerDonation);
			//If the player has taken damage, fill the tank.
			if(player.getHealth() < previousPlayerHealth) {
				if(donationEntity != null) {
					donationEntity.fill(new FluidStack(bloodFluid, mbPerDonation), true);
				}
			}
		    return true;
		}
	}
}
