package zettabyte.weirdscience.block;

import java.util.ArrayList;

import zettabyte.weirdscience.WeirdScience;
import zettabyte.weirdscience.tileentity.TileEntityBloodDonation;
import zettabyte.weirdscience.tileentity.TileEntityPhosphateEngine;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.api.Object3D;
import mekanism.common.PacketHandler;
import mekanism.common.PacketHandler.Transmission;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.util.ChargeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockBloodDonation extends BlockContainer {
	
	private Fluid bloodFluid;
	int mbPerDonation;
	int dmgPerDonation;
	int maxStorage;
	
	public BlockBloodDonation(int ID, Material Mat) {
		super(ID, Mat);
        this.setCreativeTab(WeirdScience.tabWeirdScience);
    }

	public void setFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	public void setStorageCap(int setMax) {
		maxStorage = setMax;
	}
	public void setDamagePer(int setDmg) {
		dmgPerDonation = setDmg;
	}
	public void setDonationAmt(int setAmt) {
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
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
	    float previousPlayerHealth = player.getHealth();
		player.attackEntityFrom(DamageSource.magic, (float)dmgPerDonation);
		if(player.getHealth() < previousPlayerHealth) {
			TileEntityBloodDonation donationEntity = (TileEntityBloodDonation)tileEntity;
			if(donationEntity != null) {
				donationEntity.fill(new FluidStack(bloodFluid, mbPerDonation), true);
			}
		}
	    return true;
	}

}
