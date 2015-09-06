package ws.zettabyte.weirdscience.client;

import ws.zettabyte.zettalib.client.gui.ZettaScreen;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;

public class TestGUI extends ZettaScreen {
	public static int GUI_ID = 234;

	public TestGUI() {
		// TODO Auto-generated constructor stub
    	//Test code goes here:
    	WidgetSimple widgetTest = new WidgetSimple(rootWidget);

    	widgetTest.setX(10);
    	widgetTest.setY(10);
    	widgetTest.setWidth(100);
    	widgetTest.setHeight(100);
    	
    	widgetTest.setLayer(8);

    	//Test code goes here:
    	WidgetSimple widgetTest2 = new WidgetSimple(rootWidget);

    	widgetTest2.setX(105);
    	widgetTest2.setY(105);
    	widgetTest2.setWidth(16);
    	widgetTest2.setHeight(16);
    	widgetTest2.setLayer(1);
	}

}
