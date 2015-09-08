package ws.zettabyte.zettalib.message;

import java.util.HashMap;

public class SimpleMessageHandler implements IChanneledReceiver {
	
	protected HashMap<ChannelID, IMessageReceiver> channels = new HashMap<ChannelID, IMessageReceiver>();
	
	public void addChannel(ChannelID c, IMessageReceiver m) {
		channels.put(c, m);
	}
	@Override
	public IMessageReceiver getChannel(ChannelID channel) {
		// TODO Auto-generated method stub
		return channels.get(channel);
	}
	@Override
	public Iterable<ChannelID> getChannels() {
		return channels.keySet();
	}

}
