package ws.zettabyte.zettalib.message;

//Currently is just a wrapper around strings. We might want to change that someday, though, for efficiency.
public class ChannelID implements Comparable<ChannelID> {
	public String name = "";

	public ChannelID() {}
	public ChannelID(String n) { name = n; }

	@Override
	public int compareTo(ChannelID o) {
		if(o.name.equalsIgnoreCase(name)) return 0;
		else return name.compareTo(o.name);
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof String) {
			String str = (String)o;
			return str.equalsIgnoreCase(name);
		}
		else if(o instanceof ChannelID) {
			ChannelID ch = (ChannelID)o;
			return (compareTo(ch) == 0);
		}
		return false;
	}
}
