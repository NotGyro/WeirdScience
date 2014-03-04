package zettabyte.weirdscience;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;
import zettabyte.weirdscience.block.BlockBloodDonation;
import zettabyte.weirdscience.block.BlockBloodEngine;
import zettabyte.weirdscience.block.BlockNitrateEngine;
import zettabyte.weirdscience.block.CongealedBloodBlock;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.baseclasses.BlockBase;
import zettabyte.weirdscience.core.baseclasses.BlockFluidClassicWS;
import zettabyte.weirdscience.core.baseclasses.ItemBase;
import zettabyte.weirdscience.core.baseclasses.ItemBucketBase;
import zettabyte.weirdscience.core.baseclasses.ItemFoodBase;
import zettabyte.weirdscience.core.chemistry.BlockFluidReactive;
import zettabyte.weirdscience.core.chemistry.ReactionSpec;
import zettabyte.weirdscience.core.fluid.BlockGasBase;
import zettabyte.weirdscience.core.fluid.GasFactory;
import zettabyte.weirdscience.core.fluid.GasWrapper;
import zettabyte.weirdscience.core.recipe.DisableableRecipe;
import zettabyte.weirdscience.fluid.BlockGasSmog;
import zettabyte.weirdscience.fluid.FluidAcid;
import zettabyte.weirdscience.fluid.FluidSmog;
import zettabyte.weirdscience.item.Coagulant;
import cpw.mods.fml.common.registry.GameRegistry;


public class WeirdScienceContent {
	public static final void RegisterContent(Configuration config, ContentRegistry cr) {
		//Constants.
		final int smogDetailDefault = 8;
		//Init fluids.
		FluidAcid fluidAcid = new FluidAcid("acid");
		FluidAcid fluidBase = new FluidAcid("base");
		FluidSmog fluidSmog = new FluidSmog("smog");
		Fluid fluidBlood = new Fluid("blood");
		//fluidAcid.setUnlocalizedName("fluidAcid");
		//fluidSmog.setUnlocalizedName("fluidSmog");
		//fluidBlood.setUnlocalizedName("fluidBlood");
		
		//Register fluids.
		cr.RegisterFluid(fluidAcid);
		cr.RegisterFluid(fluidBase);
		cr.RegisterFluid(fluidSmog);
		cr.RegisterFluid(fluidBlood);
		
		//Init fluid blocks.
		//Fluids used must be registered first.
		BlockFluidClassicWS acidBlock = new BlockFluidReactive(config, "Nitric Acid", fluidAcid);
		BlockFluidClassicWS baseBlock = new BlockFluidReactive(config, "Lye Solution", fluidBase);
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
		baseBlock.setTextureName("weirdscience:placeholderbase");
		bloodBlock.setTextureName("weirdscience:bloodStill");
		smogManager.setTextureName("weirdscience:smog");

		acidBlock.setUnlocalizedName("blockAcid");
		bloodBlock.setUnlocalizedName("blockBlood");
		
		smogManager.setMBMax(1024);
		
		//Give fluids block IDs and icons.
		fluidAcid.setBlockID(acidBlock.blockID);
		fluidBlood.setBlockID(bloodBlock.blockID);
		fluidBase.setBlockID(baseBlock.blockID);

		fluidAcid.setIcons(acidBlock.getIcon(0,0));
		fluidBase.setIcons(baseBlock.getIcon(0,0));
		fluidBlood.setIcons(bloodBlock.getIcon(0,0));
		
		if(smogManager.isEnabled()) {
			fluidSmog.setIcons(smogManager.blocks.get(0).getIcon(0,0));
			fluidSmog.setBlockID(smogManager.blocks.get(0).blockID);
		}
		
		//Register normal fluid blocks
		cr.RegisterBlock(acidBlock);
		cr.RegisterBlock(baseBlock);
		cr.RegisterBlock(bloodBlock);
		
		//Init & register basic blocks.
		CongealedBloodBlock congealedBlock = new CongealedBloodBlock(config, "Congealed Blood", Material.ground);
		congealedBlock.setUnlocalizedName("blockBloodCongealed");
		congealedBlock.setTextureName("weirdscience:congealedBloodBlock");
		cr.RegisterBlock(congealedBlock);
		
		BlockBase aluminumSludge = new BlockBase(config, "Aluminosilicate Sludge", Material.clay);
		aluminumSludge.setTextureName("weirdscience:aluminosilicate_sludge");
		aluminumSludge.harvestType = "shovel";
		aluminumSludge.harvestLevel = 0;
		aluminumSludge.setHardness(0.3F);
		cr.RegisterBlock(aluminumSludge);
		
		//Init & register tile-entity-bearing blocks.

		BlockNitrateEngine phosphateEngineBlock = new BlockNitrateEngine(config, "Nitrate Engine", Material.rock);
		phosphateEngineBlock.setUnlocalizedName("blockNitrateEngine");
		phosphateEngineBlock.setWaste(fluidSmog);
		cr.RegisterBlock(phosphateEngineBlock);
		
		BlockBloodEngine bloodEngineBlock = new BlockBloodEngine(config, "Hemoionic Dynamo", Material.rock);
		bloodEngineBlock.setTextureName("weirdscience:genericmachine");
		bloodEngineBlock.addTopTextureName("weirdscience:genericmachine6_off");
		bloodEngineBlock.addTopTextureName("weirdscience:genericmachine6_on");
		bloodEngineBlock.addSidesTextureName("weirdscience:genericmachine_tank_0");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_1");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_2");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_3");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_4");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_5");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_6");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_7");
		bloodEngineBlock.addSidesTextureName("weirdscience:blood_tank_8");
		cr.RegisterBlock(bloodEngineBlock);

		BlockBloodDonation donationBlock = new BlockBloodDonation(config, "Blood Donation Station", Material.rock);
		donationBlock.setUnlocalizedName("blockBloodDonation");
		donationBlock.setFluid(fluidBlood);
		donationBlock.setTextureName("weirdscience:genericmachine");
		donationBlock.addTopTextureName("weirdscience:blooddonationtop");
		donationBlock.addTopTextureName("weirdscience:blooddonationtop");
		donationBlock.addSidesTextureName("weirdscience:genericmachine_tank_0");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_1");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_2");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_3");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_4");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_5");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_6");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_7");
		donationBlock.addSidesTextureName("weirdscience:blood_tank_8");
		cr.RegisterBlock(donationBlock);
		
		//Init and register items.
		ItemFoodBase itemMelonPan = new ItemFoodBase(config, "Melonpan", ItemBase.FindFreeItemID(), 3, 0.6f);
		itemMelonPan.setTextureName("weirdscience:melonpan");
		cr.RegisterItem(itemMelonPan);
		
		Coagulant itemAlum = new Coagulant(config, "Alum", ItemBase.FindFreeItemID());
		itemAlum.setTextureName("weirdscience:coagulant");
		itemAlum.congealedBlockID = congealedBlock.blockID;
		cr.RegisterItem(itemAlum);

		ItemBucketBase bucketBlood = new ItemBucketBase(config, "Blood Bucket", ItemBase.FindFreeItemID(), bloodBlock);
		bucketBlood.setTextureName("weirdscience:bloodbucket");
		cr.RegisterItem(bucketBlood);

		ItemBucketBase bucketAcid = new ItemBucketBase(config, "Acid Bucket", ItemBase.FindFreeItemID(), acidBlock);
		bucketAcid.setTextureName("weirdscience:acidbucket");
		cr.RegisterItem(bucketAcid);
		
		//heh
		ItemBucketBase bucketBase = new ItemBucketBase(config, "Lye Solution Bucket", ItemBase.FindFreeItemID(), baseBlock);
		bucketBase.setTextureName("weirdscience:basebucket");
		cr.RegisterItem(bucketBase);
		
		ItemBase ingotAluminum = new ItemBase(config, "Aluminum Ingot", ItemBase.FindFreeItemID());
		ingotAluminum.setTextureName("weirdscience:aluminumingot");
		OreDictionary.registerOre("ingotAluminum", ingotAluminum);
		cr.RegisterItem(ingotAluminum);

		ItemBase dustAluminum = new ItemBase(config, "Aluminum Dust", ItemBase.FindFreeItemID());
		dustAluminum.setTextureName("weirdscience:aluminumdust");
		OreDictionary.registerOre("dustAluminum", dustAluminum);
		cr.RegisterItem(dustAluminum);
		
		ItemBase itemAshes = new ItemBase(config, "Ashes", ItemBase.FindFreeItemID());
		itemAshes.setTextureName("weirdscience:ashes");
		OreDictionary.registerOre("ashes", itemAshes);
		cr.RegisterItem(itemAshes);
		
		//Register recipes.
		cr.RegisterRecipe(new DisableableRecipe(itemMelonPan, new Object[]{Item.bread, Item.melon}, true, false));
		//Leaching ashes with water gives you lye!
		cr.RegisterRecipe(new DisableableRecipe(bucketBase, new Object[]{Item.bucketWater, itemAshes}, true, false));

		
		//Register chemistry.
		//Clay to slurry reaction.
		ReactionSpec clayDissolve = new ReactionSpec(fluidAcid, new ItemStack(Item.clay), aluminumSludge, null);
		clayDissolve.soluteMin = 4; //Require 4 clay.
		clayDissolve.soluteAffected = true; //Delete the clay item when the reaction takes place.
		cr.RegisterReaction(clayDissolve);
		
		//Alum to aluminum dust reaction.
		ReactionSpec alumDissolve = new ReactionSpec(fluidBase, new ItemStack(itemAlum), null, new ItemStack(dustAluminum));
		alumDissolve.soluteAffected = true;
		alumDissolve.solventAffected = false;
		cr.RegisterReaction(alumDissolve);
		
		
		ArrayList<ItemStack> aluminumIngots = OreDictionary.getOres("ingotAluminum");
		ArrayList<ItemStack> aluminumOres = OreDictionary.getOres("oreAluminum");
		aluminumOres.addAll(OreDictionary.getOres("oreBauxite"));
		//Register aluminum ingot dissolution.
		if(aluminumIngots != null) {
			if(aluminumIngots.size() > 0) {
				for(ItemStack item : aluminumIngots) {
					ReactionSpec aluminumDissolve = new ReactionSpec(fluidAcid, item.copy(), null, new ItemStack(itemAlum));
					aluminumDissolve.soluteMin = 1; //Should be 1 to 1
					aluminumDissolve.soluteAffected = true;
					aluminumDissolve.solventAffected = false;
					cr.RegisterReaction(aluminumDissolve);
				}
			}
		}
		//Register aluminum ore dissolution.
		if(aluminumOres != null) {
			if(aluminumOres.size() > 0) {
				for(ItemStack item : aluminumOres) {
					/* Note the stack size of 2: This allows item doubling for those willing to spend the effort and coal 
					 * to go the ore -> aluminosillicate slurry -> alum -> dissolved alum -> aluminum dust -> aluminum ingot path.
					 */
					ReactionSpec aluminumDissolve = new ReactionSpec(fluidAcid, item.copy(), null, new ItemStack(aluminumSludge, 2, 0));
					aluminumDissolve.soluteMin = 1; //Should be 1 to 1
					aluminumDissolve.soluteAffected = true;
					aluminumDissolve.solventAffected = false;
					cr.RegisterReaction(aluminumDissolve);
				}
			}
		}
		
		//Register furnace stuff.
		GameRegistry.addSmelting(aluminumSludge.blockID, new ItemStack(itemAlum), 0.0F);
		GameRegistry.addSmelting(dustAluminum.itemID, new ItemStack(ingotAluminum), 0.0F);//plankWood 
		//Wood to ashes smelting.
		ArrayList<ItemStack> woodPlanks = OreDictionary.getOres("plankWood");
		if(woodPlanks != null) {
			if(woodPlanks.size() > 0) {
				for(ItemStack item : woodPlanks) {
					GameRegistry.addSmelting(item.itemID, new ItemStack(itemAshes), 0.0F);
				}
			}
		}
		
	}
}
