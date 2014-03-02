package zettabyte.weirdscience.core.interfaces;
import zettabyte.weirdscience.core.ContentRegistry;
import net.minecraftforge.common.Configuration;

public interface IConfiggable {
	void doConfig(Configuration config, ContentRegistry cr);
}
