package ws.zettabyte.zettalib.thermal;

import net.minecraft.init.Blocks;
import ws.zettabyte.zettalib.thermal.registryentries.BlockPhaseChange;
import ws.zettabyte.zettalib.thermal.registryentries.FurnaceHeatBridge;

/**
 * Created by Sam on 1/2/2016.
 */
public class VanillaBlockHeat {
    public VanillaBlockHeat() { }

    public static int magmaT = 800; //Degrees celsius
    public static int fireT = 400; //Degrees celsius
    public static int iceT = -10; //Degrees celsius

    public static void init() {
        HeatRegistry hr = HeatRegistry.getInstance();
        //Any unregistered block is treated as being the same temperature as the biome ambient.
        hr.registerBlockHeat(Blocks.lava, magmaT*1000);
        hr.registerBlockHeat(Blocks.flowing_lava, magmaT*1000);
        hr.registerBlockHeat(Blocks.fire, fireT*1000);
        hr.registerBlockHeat(Blocks.ice, iceT*1000);
        hr.registerBlockHeat(Blocks.packed_ice, iceT*1000);

        BlockPhaseChange waterFreeze = new BlockPhaseChange();
        waterFreeze.to = Blocks.ice;
        waterFreeze.from = Blocks.water;
        waterFreeze.rising = false;
        waterFreeze.targetHeat = 0;
        waterFreeze.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(waterFreeze.from, waterFreeze);

        BlockPhaseChange iceMelt = new BlockPhaseChange();
        iceMelt.from = Blocks.ice;
        iceMelt.to = Blocks.water;
        iceMelt.rising = true;
        iceMelt.targetHeat = 1000; //1 degree C, Prevent a loop
        iceMelt.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(iceMelt.from, iceMelt);

        BlockPhaseChange stoneMelt = new BlockPhaseChange();
        stoneMelt.from = Blocks.stone;
        stoneMelt.to = Blocks.lava;
        stoneMelt.rising = true;
        stoneMelt.targetHeat = magmaT * 1000; //Prevent a loop
        stoneMelt.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(stoneMelt.from, stoneMelt);

        BlockPhaseChange obsMelt = new BlockPhaseChange();
        obsMelt.from = Blocks.obsidian;
        obsMelt.to = Blocks.lava;
        obsMelt.rising = true;
        obsMelt.targetHeat = (magmaT + 200) * 1000; //Prevent a loop
        obsMelt.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(obsMelt.from, obsMelt);

        BlockPhaseChange lavaFreeze = new BlockPhaseChange();
        lavaFreeze.from = Blocks.lava;
        lavaFreeze.to = Blocks.obsidian;
        lavaFreeze.rising = false;
        lavaFreeze.targetHeat = (magmaT - 200) * 1000;
        lavaFreeze.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(lavaFreeze.from, lavaFreeze);
        /*
        BlockPhaseChange flowlavaFreeze = new BlockPhaseChange();
        flowlavaFreeze.from = Blocks.flowing_lava;
        flowlavaFreeze.to = Blocks.stone;
        flowlavaFreeze.rising = false;
        flowlavaFreeze.targetHeat = (magmaT - 200) * 1000;
        flowlavaFreeze.metaSensitive = false;
        HeatRegistry.getInstance().AddBlockBehavior(flowlavaFreeze.from, flowlavaFreeze);*/

        FurnaceHeatBridge fhb = new FurnaceHeatBridge();
        HeatRegistry.getInstance().AddBlockBehavior(Blocks.furnace, fhb);
        HeatRegistry.getInstance().AddBlockBehavior(Blocks.lit_furnace, fhb);
    }
}
