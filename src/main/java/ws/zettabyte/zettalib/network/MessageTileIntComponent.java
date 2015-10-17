package ws.zettabyte.zettalib.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.IInvComponentInt;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * Primarily intended for sending messages from client to server, to notify the server that
 * the player has done something in the GUI
 */
public class MessageTileIntComponent implements IMessage {
	
	public static final int packetID = 0;

	public String componentName;
	public int val;
	public int x, y, z;
	
	public MessageTileIntComponent() {
	}
	public MessageTileIntComponent(IInvComponentInt cmp, TileEntity te) {
		x = te.xCoord; y = te.yCoord; z = te.zCoord;
		val = cmp.getComponentVal();
		componentName = cmp.getComponentName();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
        val = ByteBufUtils.readVarInt(buf, Integer.SIZE/8);
        componentName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
        ByteBufUtils.writeVarInt(buf, val, Integer.SIZE/8);
        ByteBufUtils.writeUTF8String(buf, componentName);
	}
	public static class Handler implements IMessageHandler<MessageTileIntComponent, IMessage> {
		@Override
		public IMessage onMessage(MessageTileIntComponent message,
				MessageContext ctx) {
			if(ctx.side == Side.CLIENT) return null;
			if(ctx.getServerHandler().playerEntity == null) return null;
			if(ctx.getServerHandler().playerEntity.worldObj == null) return null;
			World w = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = w.getTileEntity(message.x, message.y, message.z);
			if(te instanceof IDescriptiveInventory) {
				IDescriptiveInventory te2 = (IDescriptiveInventory)te;
				if(!(te2.canInteractWith(ctx.getServerHandler().playerEntity))) return null;
				IInvComponent c = te2.getComponent(message.componentName);
				if(c == null) return null;
				if(!(c instanceof IInvComponentInt)) return null;
				((IInvComponentInt)c).setComponentVal(message.val);
			}
			return null;
		}
	}
}
