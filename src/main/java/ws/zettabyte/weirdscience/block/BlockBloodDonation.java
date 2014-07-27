package ws.zettabyte.weirdscience.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import ws.zettabyte.weirdscience.tileentity.TileEntityBloodDonation;
import ws.zettabyte.weirdscience.core.ContentRegistry;
import ws.zettabyte.weirdscience.core.interfaces.IConfiggable;

public class BlockBloodDonation extends BlockMetaTank implements IConfiggable {
	
	private static Fluid bloodFluid;
	private static int mbPerDonation;
	private static int dmgPerDonation;

	public static void setFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	public static void setDamagePer(int setDmg) {
		dmgPerDonation = setDmg;
	}
	public static void setDonationAmt(int setAmt) {
		mbPerDonation = setAmt;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityBloodDonation TE = new TileEntityBloodDonation();
		TE.setBloodFluid(bloodFluid);
		//TE.setStorageCap(maxStorage);
		return TE;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
	    TileEntity tileEntity = world.getTileEntity(x, y, z);
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
							if(donationEntity.drain(toDrain.amount, false) != null){
								if(donationEntity.drain(toDrain.amount, false).amount >= toDrain.amount) {
									if (!player.inventory.addItemStackToInventory(filledItem)) {
										return false;
									}
								}
							}
							//Decrement our stack size.
							--playerItem.stackSize;
							//TODO: Check to see if this is actually necessary.
							player.inventory.setInventorySlotContents(player.inventory.currentItem, playerItem);
						}
						else {
							//If so, try to add the item to the player's inventory
							if(donationEntity.drain(toDrain.amount, false) != null){
								if(donationEntity.drain(toDrain.amount, false).amount >= toDrain.amount) {
									donationEntity.drain(toDrain.amount, true);
									//Set the slot to our filled item.
									player.inventory.setInventorySlotContents(player.inventory.currentItem, filledItem);
									playerItem = null;
								}
							}
						}
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
			//If the player has taken damage, fill the tank. (Prevent cheesing via fakeplayers.)
			if((player.getHealth() < previousPlayerHealth) || player.capabilities.isCreativeMode){
				if(donationEntity != null) {
					donationEntity.fillFromBlock(new FluidStack(bloodFluid, mbPerDonation), true);
				}
			}
		    return true;
		}
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		mbPerDonation = config.get("Blood", "Blood Donation Station milibuckets of blood per donation", 500).getInt();
		dmgPerDonation = config.get("Blood", "Blood Donation Station damage per donation", 2).getInt();
	}
	public BlockBloodDonation(Configuration config, String name,
			Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}
	public BlockBloodDonation(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}
	public BlockBloodDonation(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}
	
	
}
