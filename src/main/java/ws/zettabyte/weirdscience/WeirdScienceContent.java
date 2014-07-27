package ws.zettabyte.weirdscience;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ws.zettabyte.weirdscience.block.BlockBloodDonation;
import ws.zettabyte.weirdscience.block.BlockBloodEngine;
import ws.zettabyte.weirdscience.block.BlockFuelBurner;
import ws.zettabyte.weirdscience.block.BlockGunpowderEngine;
import ws.zettabyte.weirdscience.block.BlockNitrateEngine;
import ws.zettabyte.weirdscience.block.BlockOccultEngine;
import ws.zettabyte.weirdscience.block.CongealedBloodBlock;
import ws.zettabyte.weirdscience.fluid.FluidAcid;
import ws.zettabyte.weirdscience.fluid.FluidSmog;
import ws.zettabyte.weirdscience.item.Coagulant;
import ws.zettabyte.weirdscience.tileentity.TileEntityGunpowderEngine;
import ws.zettabyte.weirdscience.core.ContentRegistry;
import ws.zettabyte.weirdscience.core.SubBucket;
import ws.zettabyte.weirdscience.core.baseclasses.BlockBase;
import ws.zettabyte.weirdscience.core.baseclasses.BlockFluidClassicWS;
import ws.zettabyte.weirdscience.core.baseclasses.ItemBase;
import ws.zettabyte.weirdscience.core.baseclasses.ItemBucketWS;
import ws.zettabyte.weirdscience.core.baseclasses.ItemFoodBase;
import ws.zettabyte.weirdscience.core.chemistry.BlockFluidReactive;
import ws.zettabyte.weirdscience.core.chemistry.ReactionSpec;
import ws.zettabyte.weirdscience.core.recipe.DisableableRecipe;
import ws.zettabyte.weirdscience.core.recipe.SimpleRecipe;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

//TODO: Write some sort of generic reflection proxy class that caches the results it finds.
// ^ Actually a really strong use case for a Singleton.

public class WeirdScienceContent
{
    public static final void RegisterContent (Configuration config, ContentRegistry cr, FMLPreInitializationEvent event)
    {
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
        BlockFluidClassicWS acidBlock = new BlockFluidReactive(fluidAcid);
        BlockFluidClassicWS baseBlock = new BlockFluidReactive(fluidBase);
        BlockFluidClassicWS bloodBlock = new BlockFluidClassicWS("Blood", Material.water, fluidBlood);

        //Ugly gas init code goes here.
        /*GasWrapper smogManager = new GasWrapper(new GasFactory()
        {
            public BlockGasBase Make (Configuration config, String name, Fluid fluid)
            {
                return new BlockGasSmog(config, name, fluid);
            }
        }, "Smog", fluidSmog, smogDetailDefault);
        cr.GeneralRegister(smogManager);
        //Slaving multiple block IDs to one set of behavior is such a pain in this game.
        ((BlockGasSmog) smogManager.blocks.get(0)).setBlockAcid(acidBlock); */

        acidBlock.setBlockTextureName("weirdscience:placeholderacid");
        baseBlock.setBlockTextureName("weirdscience:placeholderbase");
        bloodBlock.setBlockTextureName("weirdscience:bloodStill");
        //smogManager.setTextureName("weirdscience:smog");

        acidBlock.setBlockName("blockAcid");
        bloodBlock.setBlockName("blockBlood");

        //smogManager.setMBMax(1024);

        //Give fluids block IDs and icons.
        fluidAcid.setBlock(acidBlock);
        fluidBlood.setBlock(bloodBlock);
        fluidBase.setBlock(baseBlock);

        if (event.getSide() == Side.CLIENT)
        {
            fluidAcid.setIcons(acidBlock.getIcon(0, 0));
            fluidBase.setIcons(baseBlock.getIcon(0, 0));
            fluidBlood.setIcons(bloodBlock.getIcon(0, 0));
        }
/*
        if (smogManager.isEnabled())
        {
            if (event.getSide() == Side.CLIENT)
            {
                fluidSmog.setIcons(smogManager.blocks.get(0).getIcon(0, 0));
            }
            fluidSmog.setBlockID(smogManager.blocks.get(0).blockID);
        }
*/
        //Register normal fluid blocks
        cr.RegisterBlock(acidBlock);
        cr.RegisterBlock(baseBlock);
        cr.RegisterBlock(bloodBlock);

        //Init & register basic blocks.
        CongealedBloodBlock congealedBlock = new CongealedBloodBlock(config, "Congealed Blood", Material.ground);
        congealedBlock.setBlockName("blockBloodCongealed");
        congealedBlock.setBlockTextureName("weirdscience:congealedBloodBlock");
        cr.RegisterBlock(congealedBlock);

        BlockBase aluminumSludge = new BlockBase(config, "Aluminosilicate Sludge", Material.clay);
        aluminumSludge.setBlockTextureName("weirdscience:aluminosilicate_sludge");
        aluminumSludge.harvestType = "shovel";
        aluminumSludge.harvestLevel = 0;
        aluminumSludge.setHardness(0.3F);
        cr.RegisterBlock(aluminumSludge);

        BlockBase blockRust = new BlockBase(config, "Rust", Material.iron);
        blockRust.setBlockTextureName("weirdscience:rustblock");
        blockRust.harvestType = "pickaxe";
        blockRust.harvestLevel = 0;
        blockRust.setHardness(0.6F);
        cr.RegisterBlock(blockRust);

        //((BlockGasSmog) smogManager.blocks.get(0)).blockRust = blockRust;
        //((BlockGasSmog) smogManager.blocks.get(0)).metaRust = 0;

        //Init & register tile-entity-bearing blocks.

        BlockNitrateEngine nitrateEngineBlock = new BlockNitrateEngine(config, "Nitrate Engine", Material.rock);
        nitrateEngineBlock.setBlockName("blockNitrateEngine");
        nitrateEngineBlock.setWaste(fluidSmog);
        cr.RegisterBlock(nitrateEngineBlock);

        BlockBloodEngine bloodEngineBlock = new BlockBloodEngine(config, "Hemoionic Dynamo", Material.rock);
        bloodEngineBlock.setBlockTextureName("weirdscience:genericmachine");
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
        donationBlock.setBlockName("blockBloodDonation");
        donationBlock.setFluid(fluidBlood);
        donationBlock.setBlockTextureName("weirdscience:genericmachine");
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

        BlockOccultEngine occultEngineBlock = new BlockOccultEngine(config, "Occult Engine", Material.rock);
        occultEngineBlock.setBlockTextureName("weirdscience:occultengine_bottom");
        occultEngineBlock.addTopTextureName("weirdscience:occultengine_top");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_empty");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_1");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_2");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_3");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_4");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_5");
        occultEngineBlock.addSidesTextureName("weirdscience:occultengine_6");
        cr.RegisterBlock(occultEngineBlock);

        BlockGunpowderEngine gunpowderEngineBlock = new BlockGunpowderEngine(config, "Blast Engine", Material.rock);
        gunpowderEngineBlock.setBlockName("blockGunpowderEngine");
        cr.RegisterBlock(gunpowderEngineBlock);

        BlockFuelBurner fuelBurnerBlock = new BlockFuelBurner(config, "Fuel Burner", Material.rock);
        fuelBurnerBlock.setBlockName("blockFuelBurner");
        fuelBurnerBlock.setBlockTextureName("weirdscience:retardcube");
        cr.RegisterBlock(fuelBurnerBlock);

        //Init and register items.
        //ItemFoodBase itemMelonPan = new ItemFoodBase(config, "Melonpan", 3, 0.6f);
        //itemMelonPan.setTextureName("weirdscience:melonpan");
        //cr.RegisterItem(itemMelonPan);

        Coagulant itemAlum = new Coagulant(config, "Alum");
        itemAlum.setTextureName("weirdscience:coagulant");
        itemAlum.congealedBlock = congealedBlock;
        cr.RegisterItem(itemAlum);

        ItemBucketWS bucket = new ItemBucketWS(config, "Bucket");
        bucket.addSubBucket(new SubBucket("Blood Bucket", "weirdscience:bloodbucket", bloodBlock));
        SubBucket bucketAcid = new SubBucket("Acid Bucket", "weirdscience:acidbucket", acidBlock);
        bucket.addSubBucket(bucketAcid);
        SubBucket bucketBase = new SubBucket("Base Bucket", "weirdscience:basebucket", baseBlock);
        bucket.addSubBucket(bucketBase);

        /*
        ItemBucketWS bucketBlood = new ItemBucketWS(config, "Blood Bucket", ItemBase.FindFreeItemID(), bloodBlock);
        bucketBlood.setTextureName("weirdscience:bloodbucket");
        cr.RegisterItem(bucketBlood);

        ItemBucketWS bucketAcid = new ItemBucketWS(config, "Acid Bucket", ItemBase.FindFreeItemID(), acidBlock);
        bucketAcid.setTextureName("weirdscience:acidbucket");
        cr.RegisterItem(bucketAcid);
        
        //heh
        ItemBucketWS bucketBase = new ItemBucketWS(config, "Lye Solution Bucket", ItemBase.FindFreeItemID(), baseBlock);
        bucketBase.setTextureName("weirdscience:basebucket");
        cr.RegisterItem(bucketBase);*/
        cr.RegisterItem(bucket);

        ItemBase ingotAluminum = new ItemBase(config, "Aluminum Ingot");
        ingotAluminum.setTextureName("weirdscience:aluminumingot");
        OreDictionary.registerOre("ingotAluminum", ingotAluminum);
        cr.RegisterItem(ingotAluminum);

        ItemBase dustAluminum = new ItemBase(config, "Aluminum Dust");
        dustAluminum.setTextureName("weirdscience:aluminumdust");
        OreDictionary.registerOre("dustAluminum", dustAluminum);
        cr.RegisterItem(dustAluminum);

        ItemBase itemAshes = new ItemBase(config, "Ashes");
        itemAshes.setTextureName("weirdscience:ashes");
        OreDictionary.registerOre("ashes", itemAshes);
        cr.RegisterItem(itemAshes);

        ItemBase itemRust = new ItemBase(config, "Rust Pile");
        itemRust.setTextureName("weirdscience:rustpile");
        cr.RegisterItem(itemRust);

        blockRust.setItemDropped(new ItemStack(itemRust, 6, 0));
        blockRust.setDroppedRandomBonus(3);

        //TODO: Thermite item behavior.
        ItemBase itemThermite = new ItemBase(config, "Thermite");
        itemThermite.setTextureName("weirdscience:thermiteitem");
        //20,000 is the fuel value of a lava bucket.
        int thermiteFuelValue = config.get("Thermite", "Furnace fuel value of Thermite", 5000).getInt();
        itemThermite.setFurnaceFuelValue(thermiteFuelValue);
        cr.RegisterItem(itemThermite);

        TileEntityGunpowderEngine.thermite = itemThermite;

        //Register recipes.

        DisableableRecipe thermiteRecipe = new DisableableRecipe(new ItemStack(itemThermite, 1, 0), new Object[] { itemRust, dustAluminum }, true, false);
        //cr.RegisterRecipe(new DisableableRecipe(itemMelonPan, new Object[] { Item.bread, Item.melon }, true, false));
        cr.RegisterRecipe(new DisableableRecipe(new ItemStack(bucket, 1, bucketBase.getAssociatedMeta()), new Object[] { Items.water_bucket, itemAshes }, true, false));
        cr.RegisterRecipe(new DisableableRecipe(new ItemStack(bucket, 1, bucketAcid.getAssociatedMeta()), new Object[] { Items.water_bucket, Items.gunpowder }, true, false));
        cr.RegisterRecipe(thermiteRecipe);
        cr.RegisterRecipe(new SimpleRecipe(new ItemStack(blockRust, 1, 0), new Object[] { "rrr", "rrr", "rrr", 'r', itemRust }, false, false));

        //Machine recipes
        //      Nitrate Engine
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(nitrateEngineBlock, 1, 0), "sss", "gcg", "sbs", Character.valueOf('s'), "blockStone", Character.valueOf('c'), new ItemStack(Items.slime_ball,
                1, 0), Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('b'), new ItemStack(Items.bucket)));
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(nitrateEngineBlock, 1, 0), "sss", "gcg", "sbs", Character.valueOf('s'), new ItemStack(Blocks.stone), Character.valueOf('c'), new ItemStack(
                Items.slime_ball, 1, 0), Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('b'), new ItemStack(Items.bucket)));
        //      Blood Donation Station
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(donationBlock, 1, 0), "aba", "aga", "aba", Character.valueOf('a'), "ingotAluminum", Character.valueOf('g'),
                new ItemStack(Blocks.glass, 1, 0), Character.valueOf('b'), new ItemStack(Items.bucket)));
        //      Blood Engine
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(bloodEngineBlock, 1, 0), "aba", "afa", "aaa", Character.valueOf('a'), "ingotAluminum", Character.valueOf('f'), new ItemStack(
                Blocks.furnace, 1, 0), Character.valueOf('b'), new ItemStack(Items.bucket)));
        //      Occult Engine
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(occultEngineBlock, 1, 0), "gog", "oeo", "gog", Character.valueOf('e'), new ItemStack(bloodEngineBlock, 1, 0), Character.valueOf('o'),
                new ItemStack(Blocks.obsidian), Character.valueOf('g'), new ItemStack(Items.gold_ingot)));
        //      Blast Engine
        cr.RegisterRecipe(new ShapedOreRecipe(new ItemStack(gunpowderEngineBlock, 1, 0), "aia", "afa", "ana", Character.valueOf('a'), "ingotAluminum", Character.valueOf('f'), new ItemStack(
                Blocks.furnace, 1, 0), Character.valueOf('n'), new ItemStack(Blocks.netherrack), Character.valueOf('i'), new ItemStack(Blocks.iron_bars)));

        if (thermiteRecipe.isEnabled())
        {
            //Registers other aluminum dusts as items from which thermite can be made.
            ArrayList<ItemStack> aluminumDusts = OreDictionary.getOres("dustAluminum");
            if (aluminumDusts != null)
            {
                if (aluminumDusts.size() > 0)
                {
                    for (ItemStack item : aluminumDusts)
                    {
                        cr.RegisterRecipe(new SimpleRecipe(new ItemStack(itemThermite, 1, 0), new Object[] { itemRust, item }, true, false));
                    }
                }
            }
        }

        //Register chemistry.
        //Clay to slurry reaction.
        ReactionSpec clayDissolve = new ReactionSpec(fluidAcid, new ItemStack(Items.clay_ball), aluminumSludge, null);
        clayDissolve.soluteMin = 4; //Require 4 clay.
        clayDissolve.soluteAffected = true; //Delete the clay item when the reaction takes place.
        cr.RegisterReaction(clayDissolve);

        //Alum to aluminum dust reaction.
        ReactionSpec alumDissolve = new ReactionSpec(fluidBase, new ItemStack(itemAlum), null, new ItemStack(dustAluminum));
        alumDissolve.soluteAffected = true;
        alumDissolve.solventAffected = false;
        cr.RegisterReaction(alumDissolve);

        //Acids and bases kill grass dead.
        ReactionSpec grassDissolveAcid = new ReactionSpec();
        grassDissolveAcid.solvent = fluidAcid;
        grassDissolveAcid.solute = Blocks.grass;
        grassDissolveAcid.soluteTarget = Blocks.dirt;
        grassDissolveAcid.solventAffected = false;
        grassDissolveAcid.soluteAffected = true;
        cr.RegisterReaction(grassDissolveAcid);

        ReactionSpec grassDissolveBase = new ReactionSpec();
        grassDissolveBase.solvent = fluidBase;
        grassDissolveBase.solute = Blocks.grass;
        grassDissolveBase.soluteTarget = Blocks.dirt;
        grassDissolveBase.solventAffected = false;
        grassDissolveBase.soluteAffected = true;
        cr.RegisterReaction(grassDissolveBase);

        ArrayList<ItemStack> aluminumIngots = OreDictionary.getOres("ingotAluminum");
        ArrayList<ItemStack> aluminumOres = OreDictionary.getOres("oreAluminum");
        //aluminumOres.addAll(OreDictionary.getOres("oreBauxite"));
        //Register aluminum ingot dissolution.
        if (aluminumIngots != null)
        {
            if (aluminumIngots.size() > 0)
            {
                for (ItemStack item : aluminumIngots)
                {
                    ReactionSpec aluminumDissolve = new ReactionSpec(fluidAcid, item.copy(), null, new ItemStack(itemAlum));
                    aluminumDissolve.soluteMin = 1; //Should be 1 to 1
                    aluminumDissolve.soluteAffected = true;
                    aluminumDissolve.solventAffected = false;
                    cr.RegisterReaction(aluminumDissolve);
                }
            }
        }
        //Register aluminum ore dissolution.
        if (aluminumOres != null)
        {
            if (aluminumOres.size() > 0)
            {
                for (ItemStack item : aluminumOres)
                {
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
        GameRegistry.addSmelting(aluminumSludge, new ItemStack(itemAlum), 0.0F);
        GameRegistry.addSmelting(dustAluminum, new ItemStack(ingotAluminum), 0.0F);//plankWood 
        //Wood to ashes smelting.
        ArrayList<ItemStack> woodPlanks = OreDictionary.getOres("plankWood");
        if (woodPlanks != null)
        {
            if (woodPlanks.size() > 0)
            {
                for (ItemStack item : woodPlanks)
                {
                    GameRegistry.addSmelting(item, new ItemStack(itemAshes), 0.0F);
                }
            }
        }
        GameRegistry.addSmelting(blockRust, new ItemStack(Blocks.iron_block, 1, 0), 0.0F);

        boolean thermiteFuelEnabled = config.get("recipe", "Enable thermite as furnace fuel", true).getBoolean(true);
        if (thermiteFuelEnabled)
        {
        }

    }
}
