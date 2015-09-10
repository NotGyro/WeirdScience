package ws.zettabyte.zettalib.block;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ws.zettabyte.zettalib.inventory.IComponentContainer;
import ws.zettabyte.zettalib.inventory.IInvComponent;

/**
 * An object to fake consistent access to a TileEntity
 * Created by Sam "Gyro" C. on 9/10/15.
 */
public class TrackTileEntity extends TileEntityBase {

    protected TileEntityBase inner = null;
    protected final BlockCoord pos;
    public TrackTileEntity(TileEntityBase toTrack) {
        pos = new BlockCoord(toTrack.xCoord, toTrack.yCoord, toTrack.zCoord);
        inner = toTrack;
        worldObj = toTrack.getWorldObj();
    };
    public TrackTileEntity(BlockCoord p, World w) {
        pos = p;
        worldObj = w;
    }

    protected void track() {
        inner = (TileEntityBase)worldObj.getTileEntity(pos.x, pos.y, pos.z);
    }

    @Override
    public World getWorldObj() {
        return inner.getWorldObj();
    }

    @Override
    public void setWorldObj(World p_145834_1_) {
        inner.setWorldObj(p_145834_1_);
    }

    @Override
    public boolean hasWorldObj() {
        return inner.hasWorldObj();
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_) {
        inner.readFromNBT(p_145839_1_);
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_) {
        inner.writeToNBT(p_145841_1_);
    }

    @Override
    public void updateEntity() {
        inner.updateEntity();
    }

    @Override
    public int getBlockMetadata() {
        return inner.getBlockMetadata();
    }

    @Override
    public void markDirty() {
        inner.markDirty();
    }

    @Override
    public double getDistanceFrom(double p_145835_1_, double p_145835_3_, double p_145835_5_) {
        return inner.getDistanceFrom(p_145835_1_, p_145835_3_, p_145835_5_);
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared();
    }

    @Override
    public Block getBlockType() {
        return inner.getBlockType();
    }

    @Override
    public Packet getDescriptionPacket() {
        return inner.getDescriptionPacket();
    }

    @Override
    public boolean isInvalid() {
        return inner.isInvalid();
    }

    @Override
    public void invalidate() {
        inner.invalidate();
    }

    @Override
    public void validate() {
        inner.validate();
    }

    @Override
    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
        return inner.receiveClientEvent(p_145842_1_, p_145842_2_);
    }

    @Override
    public void updateContainingBlockInfo() {
        inner.updateContainingBlockInfo();
    }

    @Override
    public void func_145828_a(CrashReportCategory p_145828_1_) {
        inner.func_145828_a(p_145828_1_);
    }

    @Override
    public boolean canUpdate() {
        return inner.canUpdate();
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        inner.onDataPacket(net, pkt);
    }

    @Override
    public void onChunkUnload() {
        inner.onChunkUnload();
    }

    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return inner.shouldRefresh(oldBlock, newBlock, oldMeta, newMeta, world, x, y, z);
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return inner.shouldRenderInPass(pass);
    }

    @Override
    protected TileEntity getAdjacent(ForgeDirection d) {
        return inner.getAdjacent(d);
    }

    @Override
    public void onKill() {
        inner.onKill();
    }

    @Override
    public void updateAdjacency(TileEntity te, ForgeDirection side) {
        inner.updateAdjacency(te, side);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return inner.getRenderBoundingBox();
    }

    @Override
    public Iterable<IInvComponent> getComponents() {
        return inner.getComponents();
    }

    @Override
    public IInvComponent getComponent(String name) {
        return inner.getComponent(name);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inner.canInteractWith(player);
    }
}
