package ws.zettabyte.zettalib.message;

public interface IChanneledSender {
	void registerChannel(ChannelID id, IMessageReceiver receiver);
	Iterable<ChannelID> getPossibleChannels();
}
