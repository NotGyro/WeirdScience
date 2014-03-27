package ws.zettabyte.weirdscience.block;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.zettalib.WeirdStepSound;
import ws.zettabyte.zettalib.baseclasses.BlockBase;
import ws.zettabyte.zettalib.interfaces.ISoundProvider;

public class CongealedBloodBlock extends BlockBase implements ISoundProvider {
	private static String unlocalizedName = "congealedBloodBlock";
	
	private static final String stepSoundName = WeirdScience.modid + ":step." + unlocalizedName;
	private static final String stepSoundType = "wav";
	private static final String placeSoundName = WeirdScience.modid + ":place." + unlocalizedName;
	private static final String placeSoundType = "wav";
	private static final int stepSoundCount = 2;
	private static final String stepSounds[];
	private static final int placeSoundCount = 1;
	private static final String placeSounds[];
	
	public static final ArrayList<String> sounds;
	
	static {
		stepSounds = new String[stepSoundCount];
		for(int i = 0; i < stepSoundCount; i++) {
			stepSounds[i] = WeirdScience.modid + ":step/" + unlocalizedName + (i + 1) + "." + stepSoundType;
			System.out.println("WEIRDSCIENCE DEBUG OUTPUT: " + stepSounds[i]);
		}
		placeSounds = new String[placeSoundCount];
		for(int i = 0; i < placeSoundCount; i++) {
			placeSounds[i] = WeirdScience.modid + ":place/" + unlocalizedName + (i + 1) + "." + placeSoundType;
		}
		
		sounds = new ArrayList<String>();
		sounds.addAll(Arrays.asList(stepSounds));
		sounds.addAll(Arrays.asList(placeSounds));
	}
	/*
	public CongealedBloodBlock(int blockID, Material blockMaterial) {
		super(blockID, blockMaterial);
		
		setCreativeTab(WeirdScience.tabWeirdScience);
		setUnlocalizedName("congealedBloodBlock");
		setHardness(Block.dirt.blockHardness);
		setResistance(Block.dirt.blockResistance);
		
		setStepSound((StepSound) new WeirdStepSound(placeSoundName, placeSoundName, stepSoundName, 1.0f, 1.0f));
	}*/
	
	public CongealedBloodBlock(Configuration config, String engName, Material blockMaterial) {
		super(config, engName, blockMaterial);
		//Done by the game registry.
		//BlockBase returns true from isInCreativeTab by default.
		//setCreativeTab(WeirdScience.tabWeirdScience);
		setUnlocalizedName("congealedBloodBlock");
		setHardness(Block.dirt.blockHardness);
		setResistance(Block.dirt.blockResistance);
		
		setStepSound((StepSound) new WeirdStepSound(placeSoundName, placeSoundName, stepSoundName, 1.0f, 1.0f));
	}
	/*
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		blockIcon = reg.registerIcon(WeirdScience.modid + ":" + getUnlocalizedName().substring(5));
	}*/
	@Override
	public int getHarvestLevel(int subBlockMeta) {
		return 0;
	}

	@Override
	public String getHarvestType(int subBlockMeta) {
		return "pickaxe";
	}

	@Override
	public ArrayList<String> getSounds() {
		return sounds;
	}

}