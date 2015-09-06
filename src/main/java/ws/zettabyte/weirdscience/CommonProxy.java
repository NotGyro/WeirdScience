package ws.zettabyte.weirdscience;

import java.util.HashMap;

import ws.zettabyte.weirdscience.client.TestGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{
		public CommonProxy() {
			
		}
        // Client stuff
        public void registerRenderers() {
            // TODO: Nothing. This is the server-side class.
        }
        public void registerSound() {
        	// TODO: Nothing. This is the server-side class.
        }

        @Override
        public Object getServerGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
                return null;
        }

        @Override
        public Object getClientGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
        	if(ID == TestGUI.GUI_ID) { 
        		return new TestGUI();
        	}
        	return null;
        }
}