package zettabyte.weirdscience.client;
import zettabyte.weirdscience.CommonProxy;
import zettabyte.weirdscience.EventSounds;
import zettabyte.weirdscience.WeirdScience;
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