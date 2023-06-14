package main.gui.game;


import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import main.Settings;
import main.gui.Component;

public class Overlay extends Pane implements Component {

	private Settings settings;

	@Override
	public void init() {
		this.translateXProperty().set(0);
		this.translateYProperty().set(0);
		this.setPrefSize(settings.boardWidth.get(), settings.boardHeight.get());
		initColorFade();
		initContent();
	}

	protected void initColorFade() {
		Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.TRANSPARENT) };

		LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
		this.setBackground(Background.fill(gradient));
	}

	protected void initContent() {
		
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
