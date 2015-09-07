package ws.zettabyte.weirdscience.client;

import net.minecraft.util.ResourceLocation;
import ws.zettabyte.zettalib.client.gui.CommonIcons;
import ws.zettabyte.zettalib.client.gui.ZettaScreen;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;

public class TestGUI extends ZettaScreen {
	public static int GUI_ID = 234;

	public TestGUI() {
		// TODO Auto-generated constructor stub
    	//Test code goes here:
    	WidgetSimple widgetTest = new WidgetSimple(rootWidget);

    	widgetTest.setX(0);
    	widgetTest.setY(0);
    	widgetTest.setWidth(176);
    	widgetTest.setHeight(166);
    	widgetTest.setLayer(1);
    	widgetTest.setArt(CommonIcons.backgroundInv);

    	//Test code goes here:
    	WidgetSimple widgetTest2 = new WidgetSimple(rootWidget);

    	widgetTest2.setX(105);
    	widgetTest2.setY(105);
    	widgetTest2.setWidth(16);
    	widgetTest2.setHeight(16);
    	widgetTest2.setLayer(4);
    	widgetTest2.setArt(new ResourceLocation("weirdscience", "textures/blocks/rust.png"));
	}

}
