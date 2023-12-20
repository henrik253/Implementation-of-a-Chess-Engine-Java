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

	private static final String HEADING_TEXT = "INSERT A BOARD";
	private static final String INFORMATION_TEXT = "to insert a board paste a FEN-STRING";
	private static final String WARNING = "Not a valid FEN-STRING";
	private static final String SUCCESS = "You inserted a valid FEN-STRING";

	private final Text heading = new Text(HEADING_TEXT);
	private final Text informationText = new Text(INFORMATION_TEXT);
	private TextField input = new TextField(HEADING_TEXT);

	private VBox wrapper = new VBox();
	private HBox headingWrapper = new HBox();
	private HBox informationTextWrapper = new HBox();
	private HBox inputWrapper = new HBox();
	private HBox randomFENWrapper = new HBox();

	private double parentHeight, parentWidth;

	private Settings settings;

	private Button randomFENButton, defaultFENButton;

	public InsertFENView(SettingsView settingsView, double parentHeight, double parentWidth, Settings settings) {
		this.settingsView = settingsView;

		this.parentHeight = parentHeight;
		this.parentWidth = parentWidth;

		this.setPrefHeight(parentHeight / 7);
		this.setPrefWidth(parentWidth - (parentWidth / 20));

		this.setTranslateY((parentHeight / 5) * 4);
		this.setTranslateX(parentWidth / 10);
//		this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, new Insets(1))));
		this.setId("InsertBoardView");
		init();
		initRandomFENButton();
		initDefaultFENButton();
		headingWrapper.getChildren().add(heading);
		informationTextWrapper.getChildren().add(informationText);
		inputWrapper.getChildren().add(input);
		randomFENWrapper.getChildren().addAll(randomFENButton, defaultFENButton);
		wrapper.getChildren().addAll(headingWrapper, informationTextWrapper, inputWrapper, randomFENWrapper);
		wrapper.setTranslateX(30);
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

		defaultFENButton.setOnAction(event -> {
			String defaultFEN = this.settings.defaultFENString;
			input.setText(defaultFEN);
			informationText.setText(SUCCESS);
			informationText.setFill(Color.BLACK);
			settingsView.saveFENString(defaultFEN);
		});
	}

}
