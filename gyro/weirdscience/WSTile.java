package gyro.weirdscience;

import gyro.weirdscience.tileentity.TileEntityPhosphateEngine;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WSTile {
	
	//Methods specific to our WSTile
	public WSTile() {
	}

	public boolean isTileEntity() {
		return false;
	}
	public boolean isGUIBlock() {
		return false;
	}
	public boolean hasInventory() {
		return false;
	}
	
	//Methods relayed from an associated Minecraft Block.
	public boolean hasComparatorInputOverride() {
		return false;
	}
	
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	public void breakBlock(World world, int x, int y, int z, int par5, int par6) { }
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
	    
	    return false;
	}

}
