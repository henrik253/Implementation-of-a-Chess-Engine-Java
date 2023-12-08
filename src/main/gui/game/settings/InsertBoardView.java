package main.gui.game.settings;

import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.conversions.FENConverter;
import javafx.scene.control.TextField;

public class InsertBoardView extends Pane {

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
	
	private double parentHeight,parentWidth;

	public InsertBoardView(SettingsView settingsView, double parentHeight, double parentWidth) {
		this.settingsView = settingsView;
		
		this.parentHeight = parentHeight;
		this.parentWidth = parentWidth;
		
		this.setPrefHeight(HEIGHT);
		this.setPrefWidth(WIDTH);

		this.setTranslateY((parentHeight / 5) * 4);
		this.setTranslateX(parentWidth / 5);

		this.setId("InsertBoardView");
		init();

		headingWrapper.getChildren().add(heading);
		informationTextWrapper.getChildren().add(informationText);
		inputWrapper.getChildren().add(input);
		wrapper.getChildren().addAll(headingWrapper, informationTextWrapper, inputWrapper);
		getChildren().add(wrapper);
	}

	private void init() {
		heading.setId("InsertBoardViewHeading");
		informationText.setId("InsertBoardViewInformationText");
		
		input.setMinWidth((parentWidth /8) * 6);
		input.setId("InsertBoardViewInput");
		
		input.setOnKeyTyped(event -> {
			String fen = input.getText();
			
			if(fen.isEmpty())
				return;
			
			try {
				// if no exception is thrown the fen is valid!
				FENConverter.convertPieceBoard(fen);
				informationText.setText(SUCCESS);
				settingsView.saveFENString(fen);
			} catch (Exception e) {
				informationText.setText(WARNING);
			}
			
		});
	}

}
