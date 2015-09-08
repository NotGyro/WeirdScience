package ws.zettabyte.zettalib.message;

import java.util.ArrayList;

public class MessageReceiverMultiplex implements IMessageReceiver {
	public ArrayList<IMessageReceiver> recvs = new ArrayList<IMessageReceiver>();
	public void addMessageReceiver(IMessageReceiver mr) { recvs.add(mr); }
	public Iterable<IMessageReceiver> getReceivers() { return recvs; }
	@Override
	public void receive(Object message) {
		for(IMessageReceiver r : recvs) {
			r.receive(message);
		}
	}

}
