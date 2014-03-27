package ws.zettabyte.weirdscience.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class WeirdPacketHandler implements IPacketHandler {

	public WeirdPacketHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (packet.channel.equals("WS"))
        {
            if (side == Side.SERVER) {
                handlePacketServer(packet, (EntityPlayerMP) player);
            }
            else {
                handlePacketClient(packet, (EntityPlayer) player);
            }
        }
    }

    void handlePacketClient (Packet250CustomPayload packet, EntityPlayer player) {
        //As of now this is a dummy.
    }

    void handlePacketServer (Packet250CustomPayload packet, EntityPlayerMP player) {
        //As of now this is a dummy.
    }
}
