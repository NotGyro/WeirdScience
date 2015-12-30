package ws.zettabyte.weirdscience;

import java.util.HashMap;

import ws.zettabyte.zettalib.client.gui.CommonIcons;
import ws.zettabyte.zettalib.client.gui.GUIRegistry;
import ws.zettabyte.zettalib.client.gui.GUITemplate;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetAmountBar;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetFluidBar;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetLabel;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSlot;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetTemperatureBar;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetTextField;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetTextFieldInt;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetValBar;
import ws.zettabyte.zettalib.client.render.SpriteSolidColor;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.SimpleInvComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy extends GUIRegistry {
	
	public static GUITemplate catalyticInv = new GUITemplate();
    public static GUITemplate testHeat = new GUITemplate();
    public static GUITemplate solidBurner = new GUITemplate();
	
	public CommonProxy() {
		setupCatalyticEngineGUI();
		setupTestHeatGUI();
        setupSolidBurnerGUI();
		
		catalyticInv.setGuiID(0);
		testHeat.setGuiID(1);
        solidBurner.setGuiID(2);

		this.addGUI(catalyticInv);
        this.addGUI(testHeat);
        this.addGUI(solidBurner);
	}


    private void setupSolidBurnerGUI() {
        int paneMargin = 2;
        GUITemplate inv = solidBurner;
        WidgetContainer inventoryPanel = new WidgetContainer();
        inventoryPanel.setWidth(70);
        inventoryPanel.setHeight(58);
        inv.addWidget(inventoryPanel);
        inventoryPanel.centerX();
        inventoryPanel.setY(4);

        SpriteSolidColor progressFiller = new SpriteSolidColor();
        progressFiller.r = 1.0F;
        progressFiller.a = 1.0F;
        SpriteSolidColor blackBG = new SpriteSolidColor();
        blackBG.a = 1.0F;

        //Set up inventory slots.
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 3; ++x) {
                int i = (x + (y*3));
                WidgetContainer slotPanel = new WidgetContainer(inventoryPanel);
                slotPanel.setWidth(22);
                slotPanel.setHeight(18);

                slotPanel.setX((x * slotPanel.getWidth()) + (x * paneMargin));
                slotPanel.setY((y * slotPanel.getHeight()) + (y * paneMargin));

                WidgetSlot fuelSlot = new WidgetSlot(slotPanel, "fuel" + i );
                fuelSlot.setLayer(1);
                //fuelSlot.centerX();

                WidgetValBar progress = new WidgetValBar("slotFuel" + i, slotPanel);
                progress.setWidth(2);
                progress.setHeight(18);
                progress.setX(19);
                progress.setSprite(progressFiller);
                progress.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
                progress.setLayer(4);

                WidgetSimple progBG = new WidgetSimple(slotPanel);
                progBG.setWidth(2);
                progBG.setHeight(18);
                progBG.setX(19);
                progBG.setLayer(3);
                progBG.setSprite(blackBG);
            }
        }

        WidgetContainer bar = new WidgetContainer(inv.getRootWidget());
        bar.setX(4);
        bar.setY(4);
        bar.setHeight(72);
        bar.setWidth(26);
        bar.setLayer(9);

        WidgetTemperatureBar heatBar = new WidgetTemperatureBar("temperature", bar);
        heatBar.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
        heatBar.setHeight(66);
        heatBar.setWidth(20);
        heatBar.setLayer(1);
        heatBar.setX(3);
        heatBar.setY(3);
        heatBar.setHasTooltip(false);

        WidgetSimple barBack = new WidgetSimple(bar);
        barBack.setHeight(72);
        barBack.setWidth(26);
        barBack.setLayer(0);
        barBack.setSprite(CommonIcons.tank1Background);

        WidgetSimple barFront = new WidgetSimple(bar);
        barFront.setHeight(72);
        barFront.setWidth(26);
        barFront.setLayer(2);
        barFront.setSprite(CommonIcons.tank1Overlay);
    }

    public void setupCatalyticEngineGUI() {
		int margin = 1;
		int currentHeight = 0;
		
		WidgetContainer inventoryPanel = new WidgetContainer();
		inventoryPanel.setWidth(100);
		inventoryPanel.setHeight(100);
		catalyticInv.addWidget(inventoryPanel);
		inventoryPanel.centerX();
		inventoryPanel.setY(10);

		WidgetSlot slotTest1 = new WidgetSlot(inventoryPanel, "fuel");
		slotTest1.setLayer(3);
		slotTest1.centerX();
		
		currentHeight = slotTest1.getHeight() + margin;
		
		WidgetSimple alertIcon = new WidgetSimple(inventoryPanel);
		alertIcon.setHeight(16);
		alertIcon.setWidth(22);
		alertIcon.centerX();
		alertIcon.setY(currentHeight);
		alertIcon.setLayer(3);
		alertIcon.setSprite(new ResourceLocation("weirdscience", "textures/gui/iconDanger.png"));
		
		WidgetValBar progress = new WidgetValBar("progress", inventoryPanel);
		progress.setBounds(alertIcon.getRelativeBounds());
		SpriteSolidColor progressFiller = new SpriteSolidColor();
		progressFiller.r = 1.0F;
		progressFiller.a = 1.0F;
		progress.setSprite(progressFiller);
		progress.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
		progress.setLayer(4);

		WidgetSimple alertOverlay = new WidgetSimple(inventoryPanel);
		alertOverlay.setBounds(alertIcon.getRelativeBounds());
		alertOverlay.setLayer(5);
		alertOverlay.setSprite(new ResourceLocation("weirdscience", "textures/gui/iconDangerOverlay.png"));
		
		currentHeight += alertIcon.getHeight();
		currentHeight += margin;
		
		int realX = slotTest1.getXRelative();

		
		WidgetSlot slotTest2 = new WidgetSlot(inventoryPanel, "out1");
		slotTest2.setX(realX - 10);
		slotTest2.setY(currentHeight);
		slotTest2.setLayer(3);
		
		//Comment this out and watch as the GUI is smart enough to deal sanely with this guy's absence.
		WidgetSlot slotTest3 = new WidgetSlot(inventoryPanel, "out2");
		slotTest3.setX(realX + 10);
		slotTest3.setY(currentHeight);
		slotTest3.setLayer(3);
		
		WidgetContainer tank = new WidgetContainer();
		tank.setX(4);
		tank.setY(4);
		tank.setHeight(72);
		tank.setWidth(26);
		tank.setLayer(9);
		
		WidgetFluidBar tankBar = new WidgetFluidBar("exhaust");
		tankBar.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
		tankBar.setHeight(66);
		tankBar.setWidth(20);
		tankBar.setLayer(1);
		tankBar.setX(3);
		tankBar.setY(3);
		tankBar.setHasTooltip(true);
		
		tankBar.setParent(tank);
		tank.addChild(tankBar);

		WidgetSimple tankBack = new WidgetSimple(tank);
		tankBack.setHeight(72);
		tankBack.setWidth(26);
		tankBack.setLayer(0);
		tankBack.setSprite(CommonIcons.tank1Background);
		
		WidgetSimple tankFront = new WidgetSimple(tank);
		tankFront.setHeight(72);
		tankFront.setWidth(26);
		tankFront.setLayer(2);
		tankFront.setSprite(CommonIcons.tank1Overlay);
		
		catalyticInv.addWidget(tank);
	}
	
	public void setupTestHeatGUI() {
		testHeat = new GUITemplate();
		
		WidgetContainer bar = new WidgetContainer(testHeat.getRootWidget());
		bar.setX(4);
		bar.setY(4);
		bar.setHeight(72);
		bar.setWidth(26);
		bar.setLayer(9);
		
		WidgetTemperatureBar heatBar = new WidgetTemperatureBar("temperature", bar);
		heatBar.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
		heatBar.setHeight(66);
		heatBar.setWidth(20);
		heatBar.setLayer(1);
		heatBar.setX(3);
		heatBar.setY(3);
		heatBar.setHasTooltip(true);

		WidgetSimple barBack = new WidgetSimple(bar);
		barBack.setHeight(72);
		barBack.setWidth(26);
		barBack.setLayer(0);
		barBack.setSprite(CommonIcons.tank1Background);
		
		WidgetSimple barFront = new WidgetSimple(bar);
		barFront.setHeight(72);
		barFront.setWidth(26);
		barFront.setLayer(2);
		barFront.setSprite(CommonIcons.tank1Overlay);
		
		WidgetTextFieldInt textTest = new WidgetTextFieldInt("temperature") {
            @Override
            protected int getTextInt() {
                return super.getTextInt() * 1000;
            }
		};
		textTest.setText("Set heat...");
		//textTest.setX(40);
		textTest.setY(68);
		textTest.setX(40);
		textTest.setWidth(80);
		textTest.setHeight(16);
		textTest.setMaxStringLength(16);
		textTest.setVisible(true);
		textTest.setEnabled(true);
		textTest.setLayer(200);
		
		testHeat.addWidget(textTest);
	}
	// Client stuff
	public void registerRenderers() {
		// TODO: Nothing. This is the server-side class.
	}
	public void registerSound() {
		// TODO: Nothing. This is the server-side class.
	}
}