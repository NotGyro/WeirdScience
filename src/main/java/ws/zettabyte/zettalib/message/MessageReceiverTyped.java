package ws.zettabyte.zettalib.message;

public abstract class MessageReceiverTyped<T> implements IMessageReceiver {
	protected final Class<? extends T> type;
	
	public MessageReceiverTyped(Class<? extends T> t) {
		type = t;
	}
	@Override
	public final void receive(Object message) {
		if(message.getClass().isAssignableFrom(this.type)) {
			this.recv((T)message);
		}
		else {
			throw new IllegalArgumentException(); //TODO: description
		}
	}
	
	public abstract void recv(T message);
}
