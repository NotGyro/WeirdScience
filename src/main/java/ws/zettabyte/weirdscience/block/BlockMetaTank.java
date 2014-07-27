package ws.zettabyte.weirdscience.block;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.core.baseclasses.BlockContainerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


//A block that renders some of its sides with a tank texture that varies.
//Used for the Hemoionic Dynamo and the Blood Donation Block.
public abstract class BlockMetaTank extends BlockContainerBase {

	protected ArrayList<String> tankTexNames = new ArrayList<String>(8);
    protected ArrayList<String> topTexNames = new ArrayList<String>(2);
    @SideOnly(Side.CLIENT)
	protected ArrayList<IIcon> tankIcons;
    @SideOnly(Side.CLIENT)
	protected ArrayList<IIcon> topIcons;

	public void addSidesTextureName(String str) {
		tankTexNames.add(str);
	}
	public void addTopTextureName(String str) {
		topTexNames.add(str);
	}
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister IIconRegister)
    {
    	super.registerBlockIcons(IIconRegister);
        //this.blockIcon = IIconRegister.registerIIcon(this.getTextureName()); //Equal to the line above.
    	
        //Register tank IIcons.
        tankIcons = new ArrayList<IIcon>(tankTexNames.size());
        for(int i = 0; (i < tankTexNames.size()) && (i < 16); ++i) {
        	tankIcons.add(IIconRegister.registerIcon(tankTexNames.get(i)));
        }
        //Register top IIcons.
        topIcons = new ArrayList<IIcon>(topTexNames.size());
        for(int i = 0; (i < topTexNames.size()) && (i < 16); ++i) {
        	topIcons.add(IIconRegister.registerIcon(topTexNames.get(i)));
        }
        //Fill in the blanks
        if(topIcons.size() < tankIcons.size()) {
        	IIcon last = topIcons.get(topIcons.size() - 1);
        	while(topIcons.size() < tankIcons.size()) {
        		topIcons.add(last);
        	}
        }
        //Fill in the blanks
        else if(topIcons.size() > tankIcons.size()) {
        	IIcon last = tankIcons.get(tankIcons.size() - 1);
        	while(topIcons.size() > tankIcons.size()) {
        		tankIcons.add(last);
        	}
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata) {
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
    			(fillPercent * (tankTexNames.size()-1))/100, 
    			1|2);
    }
	public BlockMetaTank(Configuration config, String name, Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}
	public BlockMetaTank(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}
	public BlockMetaTank(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}
    
    
    

}
