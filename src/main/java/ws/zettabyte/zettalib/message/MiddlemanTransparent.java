package ws.zettabyte.zettalib.message;

import java.util.ArrayList;
import java.util.HashMap;

public class MiddlemanTransparent implements IMessageMiddleman {

	protected HashMap<ChannelID, MessageReceiverMultiplex> channelsOut = new HashMap<ChannelID, MessageReceiverMultiplex>();
	protected ArrayList<IChanneledReceiver> receivers = new ArrayList<IChanneledReceiver>();
	protected ArrayList<IChanneledSender> senders = new ArrayList<IChanneledSender>();
	@Override
	public Iterable<ChannelID> getChannels() {
		return channelsOut.keySet();
	}
	@Override
	public IMessageReceiver getChannel(ChannelID channel) {
		return channelsOut.get(channel);
	}
	
	@Override
	public void registerSender(IChanneledSender s) {
		senders.add(s);
	}

	@Override
	public void registerReceiver(IChanneledReceiver r) {
		receivers.add(r);
		for(ChannelID e : r.getChannels()) {
			MessageReceiverMultiplex mpl;
			if(channelsOut.containsKey(e)) {
				mpl = channelsOut.get(e);
			}
			else {
				mpl = new MessageReceiverMultiplex();
				channelsOut.put(e, mpl);
			}
			mpl.addMessageReceiver(r.getChannel(e));
		}
	}
	@Override
	public void processMessage(ChannelID channel, Object message) {
		if(channelsOut.containsKey(channel)) {
			for(IMessageReceiver r : channelsOut.get(channel).getReceivers()) {
				r.receive(message);
			}
		}
	}
}
