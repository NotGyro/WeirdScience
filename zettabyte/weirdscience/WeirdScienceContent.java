package zettabyte.weirdscience;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.block.BlockBloodDonation;
import zettabyte.weirdscience.block.BlockBloodEngine;
import zettabyte.weirdscience.block.BlockNitrateEngine;
import zettabyte.weirdscience.block.CongealedBloodBlock;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.baseclasses.BlockFluidClassicWS;
import zettabyte.weirdscience.core.baseclasses.ItemBase;
import zettabyte.weirdscience.core.baseclasses.ItemBucketBase;
import zettabyte.weirdscience.core.baseclasses.ItemFoodBase;
import zettabyte.weirdscience.core.fluid.BlockGasBase;
import zettabyte.weirdscience.core.fluid.GasFactory;
import zettabyte.weirdscience.core.fluid.GasWrapper;
import zettabyte.weirdscience.core.recipe.DisableableRecipe;
import zettabyte.weirdscience.fluid.BlockGasSmog;
import zettabyte.weirdscience.fluid.FluidAcid;
import zettabyte.weirdscience.fluid.FluidSmog;


public class WeirdScienceContent {
	public static final void RegisterContent(Configuration config, ContentRegistry cr) {
		//Constants.
		final int smogDetailDefault = 8;
		//Init fluids.
		FluidAcid fluidAcid = new FluidAcid("acid");
		FluidSmog fluidSmog = new FluidSmog("smog");
		Fluid fluidBlood = new Fluid("blood");
		//fluidAcid.setUnlocalizedName("fluidAcid");
		//fluidSmog.setUnlocalizedName("fluidSmog");
		//fluidBlood.setUnlocalizedName("fluidBlood");
		
		//Register fluids.
		cr.RegisterFluid(fluidAcid);
		cr.RegisterFluid(fluidSmog);
		cr.RegisterFluid(fluidBlood);
		
		//Init fluid blocks.
		BlockFluidClassicWS acidBlock = new BlockFluidClassicWS(config, "Acid", fluidAcid);
		BlockFluidClassicWS bloodBlock = new BlockFluidClassicWS(config, "Blood", fluidBlood);
		
		//Ugly gas init code goes here.
		GasWrapper smogManager = new GasWrapper(new GasFactory(){
			public BlockGasBase Make(Configuration config, String name, Fluid fluid) {
				return new BlockGasSmog(config, name, fluid);
			}}, "Smog", fluidSmog, smogDetailDefault);
		cr.GeneralRegister(smogManager);
		//Slaving multiple block IDs to one set of behavior is such a pain in this game.
		((BlockGasSmog)smogManager.blocks.get(0)).setBlockAcid(acidBlock);
		
		acidBlock.setTextureName("weirdscience:placeholderacid");
		bloodBlock.setTextureName("weirdscience:bloodStill");
		smogManager.setTextureName("weirdscience:smog");

		acidBlock.setUnlocalizedName("blockAcid");
		bloodBlock.setUnlocalizedName("blockBlood");
		
		smogManager.setMBMax(1024);
		
		//Give fluids block IDs and icons.
		fluidAcid.setBlockID(acidBlock.blockID);
		fluidBlood.setBlockID(bloodBlock.blockID);

		fluidAcid.setIcons(acidBlock.getIcon(0,0));
		fluidBlood.setIcons(bloodBlock.getIcon(0,0));
		if(smogManager.isEnabled()) {
			fluidSmog.setIcons(smogManager.blocks.get(0).getIcon(0,0));
			fluidSmog.setBlockID(smogManager.blocks.get(0).blockID);
		}
		
		//Register normal fluid blocks
		cr.RegisterBlock(acidBlock);
		cr.RegisterBlock(bloodBlock);
		
		//Register fluid wrapper classes.
		//cr.DoConfig(smogManager);

		//Init & register basic blocks.
		CongealedBloodBlock congealedBlock = new CongealedBloodBlock(config, "Congealed Blood", Material.ground);
		congealedBlock.setUnlocalizedName("blockBloodCongealed");
		congealedBlock.setTextureName("weirdscience:congealedBloodBlock");
		cr.RegisterBlock(congealedBlock);
		
		//Init & register tile-entity-bearing blocks.
		BlockBloodDonation donationBlock = new BlockBloodDonation(config, "Blood Donation Station", Material.rock);
		donationBlock.setUnlocalizedName("blockBloodDonation");
		donationBlock.setFluid(fluidBlood);
		cr.RegisterBlock(donationBlock);

		BlockNitrateEngine phosphateEngineBlock = new BlockNitrateEngine(config, "Nitrate Engine", Material.rock);
		phosphateEngineBlock.setUnlocalizedName("blockNitrateEngine");
		phosphateEngineBlock.setWaste(smogManager.blocks.get(0));
		cr.RegisterBlock(phosphateEngineBlock);
		
		BlockBloodEngine bloodEngineBlock = new BlockBloodEngine(config, "Hemoionic Dynamo", Material.rock);
		bloodEngineBlock.setTextureName("weirdscience:genericmachine");
		bloodEngineBlock.addTopTextureName("weirdscience:genericmachine6_off");
		bloodEngineBlock.addTopTextureName("weirdscience:genericmachine6_on");
		bloodEngineBlock.addTankTextureName("weirdscience:genericmachine_tank_0");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_1");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_2");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_3");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_4");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_5");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_6");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_7");
		bloodEngineBlock.addTankTextureName("weirdscience:blood_tank_8");
		cr.RegisterBlock(bloodEngineBlock);
		
		//Init and register items.
		ItemFoodBase itemMelonPan = new ItemFoodBase(config, "Melonpan", ItemBase.FindFreeItemID(), 3, 0.6f);
		itemMelonPan.setTextureName("weirdscience:melonpan");
		cr.RegisterItem(itemMelonPan);

		ItemBucketBase bucketBlood = new ItemBucketBase(config, "Blood Bucket", ItemBase.FindFreeItemID(), bloodBlock);
		bucketBlood.setTextureName("weirdscience:bloodbucket");
		cr.RegisterItem(bucketBlood);
		
		ItemBucketBase bucketAcid = new ItemBucketBase(config, "Acid Bucket", ItemBase.FindFreeItemID(), acidBlock);
		bucketAcid.setTextureName("weirdscience:acidbucket");
		cr.RegisterItem(bucketAcid);
		//Register recipes.
		cr.RegisterRecipe(new DisableableRecipe(itemMelonPan, new Object[]{Item.bread, Item.melon}, true, false));
		
	}
}
