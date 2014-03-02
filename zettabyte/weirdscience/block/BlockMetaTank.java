package zettabyte.weirdscience.block;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.baseclasses.BlockContainerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


//A block that renders some of its sides with a tank texture that varies.
//Used for the Hemoionic Dynamo and the Blood Donation Block.
public abstract class BlockMetaTank extends BlockContainerBase {

	protected ArrayList<String> tankTexNames = new ArrayList<String>(8);
	protected ArrayList<Icon> tankIcons;
	protected ArrayList<String> topTexNames = new ArrayList<String>(2);
	protected ArrayList<Icon> topIcons;

	public void addTankTextureName(String str) {
		tankTexNames.add(str);
	}
	public void addTopTextureName(String str) {
		topTexNames.add(str);
	}
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
    	super.registerIcons(iconRegister);
        //this.blockIcon = iconRegister.registerIcon(this.getTextureName()); //Equal to the line above.
    	
        //Register tank icons.
        tankIcons = new ArrayList<Icon>(tankTexNames.size());
        for(int i = 0; (i < tankTexNames.size()) && (i < 16); ++i) {
        	tankIcons.add(iconRegister.registerIcon(tankTexNames.get(i)));
        }
        //Register top icons.
        topIcons = new ArrayList<Icon>(topTexNames.size());
        for(int i = 0; (i < topTexNames.size()) && (i < 16); ++i) {
        	topIcons.add(iconRegister.registerIcon(topTexNames.get(i)));
        }
        //Fill in the blanks
        if(topIcons.size() < tankIcons.size()) {
        	Icon last = topIcons.get(topIcons.size() - 1);
        	while(topIcons.size() < tankIcons.size()) {
        		topIcons.add(last);
        	}
        }
        //Fill in the blanks
        else if(topIcons.size() > tankIcons.size()) {
        	Icon last = tankIcons.get(tankIcons.size() - 1);
        	while(topIcons.size() > tankIcons.size()) {
        		tankIcons.add(last);
        	}
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return this.getIcon(side, world.getBlockMetadata(x, y, z));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	//Top
    	if(side == 1) {
    		return topIcons.get(metadata);
    	}
    	//Bottom
    	else if(side == 0) {
    		return this.blockIcon;
    	}
    	//Sides (tank)
    	else {
    		return tankIcons.get(metadata);
    	}
    }
    
    public void setMetaByFillPercent(World world, int x, int y, int z, int fillPercent) {
    	world.setBlockMetadataWithNotify(x, y, z, 
    			(fillPercent * (tankIcons.size()-1))/100, 
    			1|2);
    }
    
    //A bunch of empty constructors.
	public BlockMetaTank(Configuration config, String name, int defaultID,
			Material material) {
		super(config, name, defaultID, material);
		// TODO Auto-generated constructor stub
	}

	public BlockMetaTank(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		// TODO Auto-generated constructor stub
	}

	public BlockMetaTank(Configuration config, String name, Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockMetaTank(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

	public BlockMetaTank(int id, Material material) {
		super(id, material);
		// TODO Auto-generated constructor stub
	}


}
