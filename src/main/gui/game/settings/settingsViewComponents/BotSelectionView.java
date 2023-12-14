package main.gui.game.settings.settingsViewComponents;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.gui.game.settings.SettingsView;

public class BotSelectionView extends BorderPane { // TODO REMOVE BorderPane Wrapper, so that Image and Text
	// are independend from another

	private static final String IMAGE_PATH = "file:resources/";
	private static final String IMAGE1_NAME = "Bot1";
	private static final String IMAGE2_NAME = "Bot2";
	private static final String FILE_FORMAT = ".png";

	private static final Double OFFSET = 25.0;

	private static final Double TOPBAR_PADDING = 20.0;
	private static final Double BUTTON_PADDING = 10.0;
	private static final Double TOPBAR_SPACING = 20.0;

	private static final String BOT_NAME1 = "Bot 1";
	private static final String BOT_NAME2 = "Bot 2";

	private SettingsView settingsView;

	private HBox topBar;
	private Button button1;
	private Button button2;

	private BotRepresentation botRepresentation1; // GUI Repr. for the ChessBots
	private BotRepresentation botRepresentation2;

	private BotRepresentation selected;

	public BotSelectionView(SettingsView settingsView) { // settingsView to send buttonCalls to presenters
		this.settingsView = settingsView;
		initBotRepresentation();
		init();
		preSelect(botRepresentation1);
	}

	private void init() {
		topBar = new HBox();
		topBar.setTranslateX(OFFSET + OFFSET / 2);
		topBar.setSpacing(TOPBAR_SPACING);
		button1 = new Button("Computer 1");
		button2 = new Button("Computer 2");
		topBar.getChildren().addAll(button1, button2);
		topBar.setPadding(new Insets(TOPBAR_PADDING, TOPBAR_PADDING, TOPBAR_PADDING, TOPBAR_PADDING));
		this.setTop(topBar);

		button1.setDisable(true); // bot1 is auto selected
		this.setCenter(botRepresentation1);

		button1.setId("bot1Button");
		button1.setStyle("-fx-base: green");
		button1.setPadding(new Insets(BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING));
		button1.setOnAction(this::onActionButton1);

		button2.setId("bot2Button");
		button2.setPadding(new Insets(BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING));
		button2.setOnAction(this::onActionButton2);

	}

	private void onActionButton1(ActionEvent event) {
		button1.setDisable(true);
		button2.setDisable(false);
		this.setCenter(botRepresentation1);
	}

	private void onActionButton2(ActionEvent event) {
		button1.setDisable(false);
		button2.setDisable(true);
		this.setCenter(botRepresentation2);
	}

	private void initBotRepresentation() {
		botRepresentation1 = new BotRepresentation(this,"bot1");
		botRepresentation1.setHeading(BOT_NAME1);
		botRepresentation1.setImage(IMAGE_PATH + IMAGE1_NAME + FILE_FORMAT);
		botRepresentation1.setInformationText(
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.");

		botRepresentation2 = new BotRepresentation(this,"bot2");
		botRepresentation2.setHeading(BOT_NAME2);
		botRepresentation2.setImage(IMAGE_PATH + IMAGE2_NAME + FILE_FORMAT);
		botRepresentation2.setInformationText(
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.");

	}

	public void selectedPressed(BotRepresentation source) {
		source.setDisableSelectButton(true);

		if (source == botRepresentation1)
			botRepresentation2.setDisableSelectButton(false);
		else
			botRepresentation1.setDisableSelectButton(false);

		selected = source;

		settingsView.botSelectedPressed(source);
	}

	public void surrenderPressed() {
		settingsView.surrenderButtonPressed();
	}

	public BotRepresentation getSelectedBot() {
		return selected;
	}

	private void preSelect(BotRepresentation source) {
		selectedPressed(source);
	}

	public BotRepresentation getSelected() {
		return selected;
	}

	public void toggleSelectSurrenderButton(boolean inGame) {
		if (inGame) {
			selected.toggleSelectButton();
		} else {
			selected.toggleSurrenderButton();
		}
	}

	public void setDisableColorSelect(boolean disable) {
		selected.disableColorSelect(disable);
	}

	public void setVisibleBotSelectButtons(boolean visible) {
		topBar.getChildren().forEach(c -> c.setVisible(visible));
		if (!visible) { // without this user could be lost in menue because bot buttons disappear
			if (selected == botRepresentation1) {
				onActionButton1(new ActionEvent());
			} else {
				onActionButton2(new ActionEvent());
			}
		}
	}

}
