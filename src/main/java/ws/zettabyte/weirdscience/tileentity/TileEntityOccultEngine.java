package ws.zettabyte.weirdscience.tileentity;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import ws.zettabyte.zettalib.ContentRegistry;
import ws.zettabyte.zettalib.interfaces.IConfiggable;
import ws.zettabyte.zettalib.interfaces.IRegistrable;

public class TileEntityOccultEngine extends TileEntityBloodEngine implements IConfiggable, IRegistrable {
	
	//Static values
	protected static int staticTankCap;
	protected static float staticRfPerMB;
	protected static int staticMbPerBurn;
	protected static int staticTicksPerBurn = 20;
	protected static int staticRfPerTick;
	protected static int staticEnergyCap;
	
	//rand from 0 to this that the block will go evil in a given burn tick
	protected static int inverseChanceEvil = 10;
	protected static int inverseChanceGood = 2;
	
	protected Random rand = new Random();
	
	protected static String staticFluidName = "blood";
	protected static String engineName = "Occult Dynamo";
	
	protected boolean evilMode = false;
	
	public class IdolData {
		public int fuelMultiplier = 1;
		//Power of Minecraft explosion
		public float explPower = 4.0f;
		//Range of blocks to just straight-up set to air on engine failure.
		public int anihilationRange = 0;
		public boolean spawnWitherOnFail = false;
		//How much blood we attempt to consume in a burn tick. Also causes rf/t produced to increase, careful.
		public int hunger = 32;
		public int evilHunger = hunger * 2;
		public int evilRF = 160;
		public Block engineTo = null;
	}
	
	//Block unlocalized name to fuel multiplier
	protected static HashMap<String, IdolData> idols = new HashMap<String, IdolData>();
	protected IdolData currentIdol = null; 

	public TileEntityOccultEngine() {
		super();
	}
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		staticRfPerMB = (float)config.get(engineName, "Base RF generated per MB of fuel", 0.05f).getDouble(0.05d);
		staticRfPerTick = config.get(engineName, "RF transfer rate", 80).getInt();
		staticEnergyCap = config.get(engineName, "Capacity of internal energy buffer", 16000).getInt();
		tankCap = config.get(engineName, "Internal fuel tank capacity", 4000).getInt();
		staticMbPerBurn = 400; //Amount of fuel to attempt to consume at once.
	    staticTicksPerBurn = 20; //Time between ticks where we burn fuel. To reduce lag.
		ticksUntilBurn = staticTicksPerBurn;
		energyCap = staticEnergyCap;
		
		//Idols:
		//Dragon egg
		IdolData eggData = new IdolData();
		eggData.fuelMultiplier = 96;
		eggData.engineTo = Block.lavaStill;
		eggData.explPower = 4.0f;
		eggData.evilHunger = staticMbPerBurn;
		eggData.evilRF = staticRfPerTick * 2;
		idols.put(Block.dragonEgg.getUnlocalizedName(), eggData);
		
		//wither skull
		IdolData skullData = new IdolData();
		skullData.fuelMultiplier = 32;
		skullData.explPower = 4.0f;
		skullData.evilHunger = staticMbPerBurn;
		skullData.evilRF = staticRfPerTick * 2;
		skullData.spawnWitherOnFail = true;
		skullData.anihilationRange = 2;
		idols.put(Block.skull.getUnlocalizedName(), skullData);
		
		
		
	}

	@Override
	public String getEnglishName() {
		return engineName;
	}

	@Override
	public String getGameRegistryName() {
		return engineName.replace(" ", "");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public void updateCurrentIdol(String unlocalizedName) {
		//If we're in evil mode and try to switch out the idol, bad things happen.
		if(evilMode) {
			doFailure();
			return;
		}
		//Is it valid?
		if(unlocalizedName != null) {
			//Is it registered?
			if(idols.containsKey(unlocalizedName)) { 
				currentIdol = idols.get(unlocalizedName);
			}
			else {
				currentIdol = null;
			}
		}
		else {
			currentIdol = null;
		}
	}


	//ENTITY UPDATE:
	@Override
	public void updateEntity() {
		tryInit();
		//Clientside is for suckers.
		if(!worldObj.isRemote) {
			//Are we still waiting to burn fuel?
			boolean flagHasPower = energy > 0;
	        if (this.ticksUntilBurn > 0) {
	        	--this.ticksUntilBurn;
	        }
	        else {
				ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
				//Are we generating, rather than drawing, power?
				if(!evilMode) {
					if(currentIdol != null) {
						//Burn logic:
						//Do we have fuel?
						if (this.tank != null) {
							if ((this.tank.amount >= 1) && (energy < energyCapStatic)) {
								int toBurn = Math.min(currentIdol.hunger, this.tank.amount); //Either eat mbPerBurn fuel or the entire stack.
								drain(ForgeDirection.UP, toBurn, true);
						           	
								energy += (int)(((float)toBurn)*staticRfPerMB) * currentIdol.fuelMultiplier;
								if(energy > energyCapStatic) {
									energy = energyCapStatic;
								}
								flagHasPower = true;
								if(rand.nextInt(inverseChanceEvil) == 0) {
									//It begins.
									evilMode = true;
								}
							}
						}
					}
			        if(flagHasPower) {
			    		//And now, attempt to charge surrounding blocks.
			            this.powerAdjacent();
			        }
		        }
				//We're draining power and kinda evil right now. Watch out.
				else {
					if(currentIdol != null) {
						if(tank == null) {
							doFailure();
							return;
						}
						energy -= currentIdol.evilRF;
						tank.amount -= currentIdol.evilHunger;
						if(energy <= 0) {
							doFailure();
							return;
						}
						if(tank.amount <= 0) {
							doFailure();
							return;
						}
					}
					else {
						doFailure();
						return;
					}

					if(rand.nextInt(inverseChanceGood) == 0) {
						evilMode = false;
					}
				}
	        }
		}
	}
	//Something went wrong while we were in evil mode:
	protected void doFailure() {
		if(!worldObj.isRemote) {
			if(currentIdol != null) {
				int aRange = currentIdol.anihilationRange;
				//Do we have an anihilation range?
				if(aRange > 0) {
					//Iterate through...
					for(int xr = -aRange; xr <= aRange; ++xr) {
						for(int yr = -aRange; yr <= aRange; ++yr) {
							for(int zr = -aRange; zr <= aRange; ++zr) {
								///...clearing a space.
								if(Block.blocksList[worldObj.getBlockId(xCoord + xr, yCoord + yr, zCoord + zr)] != null) {
									if(Block.blocksList[worldObj.getBlockId(xCoord + xr, yCoord + yr, zCoord + zr)]
											.canEntityDestroy(worldObj, xCoord + xr, yCoord + yr, zCoord + zr, null)
											&& !Block.blocksList[worldObj.getBlockId(xCoord + xr, yCoord + yr, zCoord + zr)]
													.getUnlocalizedName().contentEquals(Block.bedrock.getUnlocalizedName())) {
										worldObj.setBlockToAir(xCoord + xr, yCoord + yr, zCoord + zr);
									}
								}
							}
						}
					}
				}
				//Do we have an explosion?
				if(currentIdol.explPower > 0.3f) {
					worldObj.createExplosion(null, xCoord, yCoord, zCoord, currentIdol.explPower, true);
				}
				//Do we have an entity to spawn?
				if((currentIdol.spawnWitherOnFail == true) && (worldObj.difficultySetting > 0)) {
					EntityWither entitywither = new EntityWither(worldObj);
	                entitywither.setLocationAndAngles((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 0.0F, 0.0F);
	                entitywither.func_82206_m();
	                worldObj.spawnEntityInWorld(entitywither);
				}
				//Do we have something special to switch this block to?
				if(currentIdol.engineTo != null) {
					worldObj.setBlock(xCoord, yCoord, zCoord, currentIdol.engineTo.blockID);
				}
				//...otherwise, set it to air.
				else {
					worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				}
			}
		}
	}
	//Allow receiving energy so that players can prevent disasters.
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int toReceive = Math.min(maxReceive, (energyCap - energy));
		if(!simulate) {
			energy += toReceive; 
		}
		return toReceive;
	}
}
