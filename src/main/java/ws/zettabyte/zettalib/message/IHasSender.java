package ws.zettabyte.zettalib.message;

public interface IHasSender extends IChanneledSender {
	IChanneledSender getMessageSender();
	default void registerChannel(ChannelID id, IMessageReceiver receiver) {
		getMessageSender().registerChannel(id, receiver);
	}
}
