package zettabyte.weirdscience.core.fluid;

import java.util.ArrayList;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IRegistrable;

public class GasWrapper implements IRegistrable, IConfiggable {

	protected String englishName;
	protected String gameRegName;
	protected int detail;
	public ArrayList<BlockGasBase> blocks;
	protected ArrayList<Integer> blockIDs;
	protected Fluid ourFluid;
	protected GasFactory ourGasFac; 
	public GasWrapper(GasFactory gas, String engName, Fluid fluid, int detailDefault) {
		englishName = engName;
		gameRegName = "gas" + engName.replace(" ", "");
		ourFluid = fluid;
		detail = detailDefault;
		ourGasFac = gas;
		blocks = new ArrayList<BlockGasBase>(detailDefault); //The most helpful size hint.
		blockIDs = new ArrayList<Integer>(detailDefault); //The most helpful size hint.
	}

	//The meat of our gas wrapper class. Builds the gas.
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		detail = config.get("Block", englishName + " gas detail", detail).getInt();
		try {
			if(isEnabled()) {
		        for(int i = 0; i < detail; i++) {
		        	BlockGasBase current = ourGasFac.Make(config, englishName + i, ourFluid);
		        	//i.e. gasSmogBlock4
		        	//current.setUnlocalizedName(gameRegName + "Block" + i);
		        	blocks.add(current);
		        	blockIDs.add(current.blockID);
		        	cr.logger.info("Added ID " + current.blockID + " to gas: " + englishName);
		        	
		        	cr.RegisterBlock(current);
		        }
			}
		} catch (Exception e) {
			cr.logger.severe(e.getMessage());
			e.printStackTrace();
		}
		//Add all of the IDs to all of the instances.
        for(int i = 0; i < detail; i++) {
            for(int j = 0; j < detail; j++) {
            	blocks.get(i).addExtenderID(blockIDs.get(j));
            }
        }
	}
	
	@Override
	public String getEnglishName() {
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		return gameRegName;
	}

	@Override
	public boolean isEnabled() {
		return (detail != 0);
	}
	
	public void setTextureName(String string) {
        for(int i = 0; i < detail; i++) {
        	blocks.get(i).setTextureName(string);
        }
	}
	
	public void setEntitiesInteract(boolean b) {
        for(int i = 0; i < detail; i++) {
        	blocks.get(i).setEntitiesInteract(b);
        }
	}

	public void setMBMax(int max) {
	    for(int i = 0; i < detail; i++) {
	    	blocks.get(i).setMBMax(max);
	    }
	}

	public void setHardness(float h) {
	    for(int i = 0; i < detail; i++) {
	    	blocks.get(i).setHardness(h);
	    }
	}

}
