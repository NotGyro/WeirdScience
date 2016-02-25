package ws.zettabyte.zettalib.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.IInvComponentInt;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
		x = te.getPos().getX(); y = te.getPos().getY(); z = te.getPos().getZ();
		val = cmp.getComponentVal();
		componentName = cmp.getComponentName();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
        val = buf.readInt();
        componentName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(val);
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
			TileEntity te = w.getTileEntity(new BlockPos(message.x, message.y, message.z));
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
