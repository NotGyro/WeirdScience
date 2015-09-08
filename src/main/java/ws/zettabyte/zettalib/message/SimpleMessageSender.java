package ws.zettabyte.zettalib.message;

import java.util.HashMap;

public class SimpleMessageSender implements IChanneledSender {

	protected HashMap<ChannelID, IMessageReceiver> channels = new HashMap<ChannelID, IMessageReceiver>();
	
	@Override
	public void registerChannel(ChannelID c, IMessageReceiver m) {
		channels.put(c, m);
	}
	
	public void send(ChannelID c, Object message) {
		if(channels.containsKey(c)) {
			channels.get(c).receive(message);
		} else {
			//TODO: Logging or an exception or something here.
		}
	}

	@Override
	public Iterable<ChannelID> getPossibleChannels() {
		return channels.keySet();
	}
}
