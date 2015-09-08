package ws.zettabyte.zettalib.message;

public interface IChanneledReceiver {
	IMessageReceiver getChannel(ChannelID channel);
	Iterable<ChannelID> getChannels();
	default void processMessage(ChannelID channel, Object message) {
		this.getChannel(channel).receive(message);
	}
}
