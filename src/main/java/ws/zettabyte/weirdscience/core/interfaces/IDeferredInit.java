package ws.zettabyte.weirdscience.core.interfaces;

import ws.zettabyte.weirdscience.core.ContentRegistry;

/**
 * Doesn't appear to work. Do not use.
 */
public interface IDeferredInit {
	//To be called after every configgable has been configured.
	void DeferredInit(ContentRegistry cr);
}
