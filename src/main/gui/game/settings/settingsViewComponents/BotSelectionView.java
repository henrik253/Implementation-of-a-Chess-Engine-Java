package main.gui.game.settings.settingsViewComponents;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BotSelectionView extends BorderPane {

	private static final String IMAGE_PATH = "file:resources/";
	private static final String FILE_FORMAT = ".png";

	private HBox topBar;
	private Button button1;
	private Button button2;

	private BotRepresentationView botRepresentationView1; // GUI Repr. for the ChessBots
	private BotRepresentationView botRepresentationView2;

	public BotSelectionView() {
		initBots();
		init();
	}

	private void init() {
		topBar = new HBox();
		button1 = new Button(" Computer 1");
		button2 = new Button(" Computer 2");
		topBar.getChildren().addAll(button1, button2);
		this.setTop(topBar);
		
		button1.setDisable(true); // bot1 is auto selected
		this.setCenter(botRepresentationView1);
		
		button1.setOnAction(event -> {
			button1.setDisable(true);
			button2.setDisable(false);
			this.setCenter(botRepresentationView1);
		});

		button2.setOnAction(event -> {
			button1.setDisable(false);
			button2.setDisable(true);
			this.setCenter(botRepresentationView2);
		});
	}

	private void initBots() {
		botRepresentationView1 = new BotRepresentationView(this);
		botRepresentationView1.setHeading("Computer 1");
		botRepresentationView1.setImage(IMAGE_PATH + "Bot1" + FILE_FORMAT);
		botRepresentationView1.setInformationText(
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.");

		botRepresentationView2 = new BotRepresentationView(this);
		botRepresentationView2.setHeading("Computer 2");
		botRepresentationView2.setImage(IMAGE_PATH + "Bot2" + FILE_FORMAT);
		botRepresentationView2.setInformationText(
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.");

	}

	public void selectedPressed(String source) {
		System.out.println(source);
	}

}
