package ws.zettabyte.weirdscience.client;
import ws.zettabyte.weirdscience.CommonProxy;
import ws.zettabyte.weirdscience.EventSounds;
import ws.zettabyte.weirdscience.WeirdScience;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
       
        @Override
        public void registerRenderers() {
        	//Todo: rendering things.
        }
       
        @Override
        public void registerSound() {
        	System.out.println("WEIRDSCIENCE DEBUG: REGISTER SOUND CALLED");
        	MinecraftForge.EVENT_BUS.register(new EventSounds(WeirdScience.sounds));
        }
}