package main.gui.game.sidebar;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Settings;
import utils.conversions.FENConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InsertFENView extends Pane {

	private static SettingsView settingsView;

	private static final double WIDTH = 200.0;
	private static final double HEIGHT = 200.0;

	private static final String HEADING_TEXT = "Insert a FEN-String";
	private static final String INFORMATION_TEXT = "to generate a custom Board, paste a FEN-String";
	private static final String WARNING = "Not a valid FEN-STRING";
	private static final String SUCCESS = "You inserted a valid FEN-STRING";

	private final Text heading = new Text(HEADING_TEXT);
	private final Text informationText = new Text(INFORMATION_TEXT);
	private TextField input = new TextField(HEADING_TEXT);

	private VBox wrapper = new VBox(10);
	private HBox headingWrapper = new HBox(5);
	private HBox informationTextWrapper = new HBox(5);
	private HBox inputWrapper = new HBox();
	private HBox buttonWrapper = new HBox(10);

	private double parentHeight, parentWidth;

	private Settings settings;

	private Button randomFENButton, defaultFENButton;

	public InsertFENView(SettingsView settingsView, double parentHeight, double parentWidth, Settings settings) {
		this.settingsView = settingsView;
		this.parentHeight = parentHeight;
		this.parentWidth = parentWidth;

		this.setPrefHeight(parentHeight / 5);
		this.setPrefWidth(parentWidth - (parentWidth / 8));

		this.setTranslateY((parentHeight / 5) * 3);
		this.setTranslateX(parentWidth / 10);
//		this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, new Insets(1))));
		this.setId("InsertBoardView");
		init();
		initRandomFENButton();
		initDefaultFENButton();
		headingWrapper.getChildren().add(heading);
		informationTextWrapper.getChildren().add(informationText);
		inputWrapper.getChildren().add(input);
		buttonWrapper.getChildren().addAll(randomFENButton, defaultFENButton);
		wrapper.getChildren().addAll(headingWrapper, informationTextWrapper, inputWrapper, buttonWrapper);
		wrapper.setTranslateX(10);
		wrapper.setTranslateY(10);
		getChildren().add(wrapper);

		this.settings = settings;
	}

	private void init() {
		heading.setId("InsertBoardViewHeading");
		informationText.setId("InsertBoardViewInformationText");

		input.setMinWidth((parentWidth / 8) * 6);
		input.setId("InsertBoardViewInput");

		input.setOnKeyTyped(event -> {
			String fen = input.getText();

			if (fen.isEmpty())
				return;

			try {
				// if no exception is thrown the fen is valid!
				FENConverter.convertToPieceBoard(fen);
				informationText.setText(SUCCESS);
				informationText.setFill(Color.BLACK);
				settingsView.saveFENString(fen);
			} catch (Exception e) {
				informationText.setText(WARNING);
				informationText.setFill(Color.RED);
			}

		});
	}

	private void initRandomFENButton() {
		randomFENButton = new Button("RANDOM");
		randomFENButton.setId("InsertBoardViewRandomButton");
		randomFENButton.setOnAction(event -> {
			int index = (int) (Math.random() * this.settings.fenExamples.length);
			String randFEN = this.settings.fenExamples[index];
			input.setText(randFEN);
			informationText.setText(SUCCESS);
			informationText.setFill(Color.BLACK);
			settingsView.saveFENString(randFEN);
		});
	}

	private void initDefaultFENButton() {
		defaultFENButton = new Button("DEFAULT");
		defaultFENButton.setId("InsertBoardViewDefaultFENButton");
		defaultFENButton.setOnAction(event -> {
			String defaultFEN = this.settings.defaultFENString;
			input.setText(defaultFEN);
			informationText.setText(SUCCESS);
			informationText.setFill(Color.BLACK);
			settingsView.saveFENString(defaultFEN);
		});
	}

}
