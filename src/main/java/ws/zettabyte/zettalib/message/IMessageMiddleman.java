package ws.zettabyte.zettalib.message;

/*
 * Sending over different middlemen allows messages to be sent over the network vs locally, for example.
 */
public interface IMessageMiddleman extends IChanneledReceiver {
	public void registerSender(IChanneledSender s);
	public void registerReceiver(IChanneledReceiver r);
}
