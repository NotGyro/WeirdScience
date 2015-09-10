package ws.zettabyte.zettalib.client.render;

public interface ISpriteSized extends ISprite {

	/**
	 * Get the size of the texture, if this sprite is an image.
	 */
	int getNativeWidth();
	/**
	 * Get the size of the texture, if this sprite is an image.
	 */
	int getNativeHeight();
}
