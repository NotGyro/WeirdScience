package ws.zettabyte.zettalib.message;

public interface IHasReceiver extends IChanneledReceiver {
	IChanneledReceiver getMessageReceiver();
	
	default IMessageReceiver getChannel(ChannelID channel) {
		return getMessageReceiver().getChannel(channel);
	};
	default void processMessage(ChannelID channel, Object message) {
		getMessageReceiver().processMessage(channel, message);
	}
}
