package ws.zettabyte.weirdscience.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.zettalib.block.BlockGeneric;
import ws.zettabyte.zettalib.block.CubeIconSet;
import ws.zettabyte.zettalib.block.RotationTools;
import ws.zettabyte.zettalib.thermal.IIgnitable;

/**
 * Created by Sam "Gyro" C. on 12/21/2015.
 */
public class BlockIgniter extends BlockGeneric {
    @SideOnly(Side.CLIENT)
    public CubeIconSet icons = new CubeIconSet();

    public BlockIgniter(Material material) {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        //Porting danger spot
        ForgeDirection facing = ForgeDirection.VALID_DIRECTIONS[meta & ~8];
        ForgeDirection sideDir = ForgeDirection.VALID_DIRECTIONS[side];

        return icons.getIconPistonStyle(sideDir, facing);
    }
    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z,
                               ForgeDirection axis) {
        // Dumb hacks ahoy. Should really find a better (but still non-verbose)
        // way to do this.
        return RotationHelper.rotateVanillaBlock(Blocks.dispenser, worldObj,
                x, y, z, axis);
    }
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase placer, ItemStack thisItemStack) {
        super.onBlockPlacedBy(world, x, y, z, placer, thisItemStack);
        if(world.isRemote) return;
        RotationTools.initSixDirBlock(world, x, y, z, placer);
        System.out.println("Dir: " + this.getFacing(world,x,y,z).name());
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons.setAllSidesName("weirdscience:genericmachine");
        icons.setTextureName("weirdscience:igniterface", ForgeDirection.SOUTH);
        icons.registerBlockIcons(iconRegister);
    }

    public ForgeDirection getFacing(World world, int x, int y, int z) {
        return ForgeDirection.VALID_DIRECTIONS[world.getBlockMetadata(x,y,z) & ~8];
    }
    //Possibly "get icon for item render"?
    @SideOnly(Side.CLIENT)
    public IIcon func_149735_b(int p_149735_1_, int p_149735_2_) {
        return this.getIcon(p_149735_1_, 1);
    }

    protected boolean isOnCooldown(World world, int x, int y, int z, Block b) {
        return !((world.getBlockMetadata(x,y,z) & 8) == 0); //If the 4th bit (the 8s place) is set, we're on cooldown
    }
    protected void startCooldown(World world, int x, int y, int z, Block b) {
        //Set our "cooldown" flag
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 0);
    }
    protected void endCooldown(World world, int x, int y, int z, Block b) {
        //Set our "cooldown" flag to 0 by &ing with its inverse
        world.setBlockMetadataWithNotify(x,y,z, world.getBlockMetadata(x,y,z) & ~8, 0);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        super.onNeighborBlockChange(world, x, y, z, b);
        if(world.isRemote) return;

        if((b == null) || (world.isAirBlock(x,y,z))) return;
        if(b == Blocks.fire) return;

        //Are we receiving power?
        if(this.isRedstoneActive(world,x,y,z)) {
            if( ! isOnCooldown(world,x,y,z, this)){
                //First, check to see if the block in front of ours is air.
                ForgeDirection dir = this.getFacing(world, x, y, z);
                System.out.println("Dir: " + dir.name());
                int xShift = x + dir.offsetX;
                int yShift = y + dir.offsetY;
                int zShift = z + dir.offsetZ;
                Block faced = world.getBlock(xShift, yShift, zShift);
                if (faced == Blocks.fire) return;
                if (faced.isAir(world, xShift, yShift, zShift)) {
                    //We've found an air block
                    //syShift -= 1; //Move down to check flammability.
                    //Block floor = world.getBlock(x,y,z);
                    //if(floor.isFlammable(world,x,y,z)) {
                    // }

                    //Set our "cooldown" flag (MUST BE BEFORE setBlock TO PREVENT STACK OVERFLOW SHENANIGANS):
                    startCooldown(world, x, y, z, this);

                    world.setBlock(xShift, yShift, zShift, Blocks.fire);
                }
                else if (faced instanceof IIgnitable) {
                    IIgnitable burner = (IIgnitable) faced;
                    startCooldown(world,x,y,z, this);

                    burner.ignite(world, xShift, yShift, zShift);
                }
            }
        }
        else {
            if((world.getBlockMetadata(x,y,z) & 8) > 0) {
                //Lost power, reset ourselves to work again.
                endCooldown(world,x,y,z, this);
            }
        }
    }
    boolean isRedstoneActive(World world, int x, int y, int z) {
        if(world.getBlockPowerInput(x, y, z) > 0) return true;
        if(world.isBlockIndirectlyGettingPowered(x,y,z)) return true;
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        System.out.println(world.getBlockMetadata(x,y,z));
        return super.onBlockActivated(world, x, y, z, p_149727_5_, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }
}
