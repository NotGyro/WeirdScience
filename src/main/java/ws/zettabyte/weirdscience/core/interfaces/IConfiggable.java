package ws.zettabyte.weirdscience.core.interfaces;
import ws.zettabyte.weirdscience.core.ContentRegistry;
import net.minecraftforge.common.config.Configuration;

public interface IConfiggable {
	void doConfig(Configuration config, ContentRegistry cr);
}
