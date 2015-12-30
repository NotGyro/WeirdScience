package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;

import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.inventory.IComponentReceiver;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.IInvComponentInt;

public class WidgetTextFieldInt extends WidgetTextField implements
		IComponentReceiver {
	protected String seeking;
	private final ArrayList<String> container = new ArrayList<String>(1);
	protected IInvComponentInt component = null;
	
	public WidgetTextFieldInt() {
		super();
	}
	public WidgetTextFieldInt(String compName) {
		this();
		setComponentSought(compName);
	}

	public void setComponentSought(String s) { 
		seeking = s;
		container.clear();
		container.add(seeking);
	}
	@Override
	public Iterable<String> getComponentsSought() {
		return container;
	}

	@Override
	public void provideComponent(IInvComponent comp) {
		if(comp.getComponentName().equalsIgnoreCase(seeking)) {
			if(comp instanceof IInvComponentInt) {
				component = (IInvComponentInt) comp;
			}
		}
	}

	protected int getTextInt() {
		return Integer.parseInt(this.getText());
	}
	protected void writeToComponent() {
		if(this.component != null) {
			if((this.getText() == null ) || (this.getText().equalsIgnoreCase(""))) {
				this.component.setComponentVal(0);
			}
			else {
				try {
					this.component.setComponentVal(this.getTextInt());
				}
				catch (Exception e) {
					this.component.setComponentVal(0);
				}
			}
		}
	};
	
	
	@Override
	public void writeText(String in) {
		String put = sanitize(in);
		if(put.length() > 0) {
			super.writeText(put);
		}
	}
	
	private static String sanitize(String s) {

        StringBuilder stringbuilder = new StringBuilder();
        char[] achar = s.toCharArray();
        int i = achar.length;
        
        boolean encounteredNumber = false;

        for (int j = 0; j < i; ++j)
        {
            char c = achar[j];
            if( (c == '0') ||
            	(c == '1') ||
            	(c == '2') ||
            	(c == '3') ||
            	(c == '4') ||
            	(c == '5') ||
            	(c == '6') ||
            	(c == '7') ||
            	(c == '8') ||
            	(c == '9') ) {
                stringbuilder.append(c);
                encounteredNumber = true;
            }
            else if((!encounteredNumber) && (c == '-') ) {
                stringbuilder.append(c);
            }
        }
        return stringbuilder.toString();
	}
	@Override
	public void setFocused(boolean b) {
		super.setFocused(b);
		//This text box lost focus, send our packet or whatever
		if(b == false) {
			this.writeToComponent();
		}
	}
	
	@Override
	protected WidgetTextField newThis() {
		return (WidgetTextField) new WidgetTextFieldInt();
	}

	@Override
	public IGUIWidget copy() {
		WidgetTextFieldInt clone = (WidgetTextFieldInt) super.copy();
		clone.setComponentSought(seeking);
		return clone;
	}
	
}
