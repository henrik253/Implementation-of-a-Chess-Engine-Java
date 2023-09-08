package main.gui.game.settings.settingsViewComponents;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.model.chessPieces.ChessPieceColor;

public class BotRepresentation extends Pane {

	
	private static final Double OFFSET = 25.0;
	private static final Double PADDING = 10.0;
	private static final Double IMAGE_WIDTH = 64.0;
	private static final Double IMAGE_HEIGHT = 64.0;
	
	private static final Double IMAGE_HEADING_OFFSET = 15.0;
	private static final Double INFO_TEXT_OFFSET = 20.0;
	private static final Double WINDOW_WIDTH = 400 - OFFSET * 2;
	
	private static final String SELECT_BUTTON_TEXT1 = "select";
	private static final String SELECT_BUTTON_TEXT2 = "selected";
	
	private Pane imageWrapper = new Pane();
	private ImageView imageView = new ImageView();
	private Text heading = new Text("Heading");
	private Text informationText = new Text("");

	private Button selectButton = new Button("Select");
	private Button surrenderButton = new Button("surrender");
	private Button remisButton = new Button("Remis");

	private BotSelectionView botSelectionView;
	
	private ChessPieceColor selectedColor; 
	
	public BotRepresentation(BotSelectionView botSelectionView) {
		this.botSelectionView = botSelectionView;
		selectedColor = ChessPieceColor.BLACK; // By default black 
		init();
	

	}

	private void init() { // TODO Select button is going to change to surr & Remi B. when game is Running
		this.setId("BotRepresentationMenue");
		this.setTranslateY(OFFSET * 2);
		this.setTranslateX(OFFSET + OFFSET / 2);
		this.setPrefSize(WINDOW_WIDTH,200);
		
		
		imageWrapper.getChildren().add(this.imageView);
		this.imageWrapper.setTranslateY(-OFFSET);
		this.imageWrapper.setTranslateX(-OFFSET);
		this.getChildren().add(imageWrapper);
		
		this.getChildren().add(heading);
		this.heading.setTranslateX(IMAGE_WIDTH / 2
				+ IMAGE_HEADING_OFFSET );
		this.heading.setTranslateY(IMAGE_HEIGHT / 2);
		this.heading.setId("BotHeadingText");
		
		this.getChildren().add(informationText);
	
		this.informationText.setTranslateY(IMAGE_HEIGHT + INFO_TEXT_OFFSET);
		this.informationText.setTranslateX(PADDING);
		this.informationText.setWrappingWidth(WINDOW_WIDTH - OFFSET);
		
		this.getChildren().add(selectButton);
		
		this.selectButton.setTranslateY( this.getPrefHeight() - OFFSET * 2);
		this.selectButton.setMinWidth(WINDOW_WIDTH / 2);
		this.selectButton.setTranslateX(WINDOW_WIDTH / 4);
		this.selectButton.setId("BotSelectButton");
		
		selectButton.setOnAction(event -> {
			botSelectionView.selectedPressed(this);
		});
		
		
		this.setBackground(Background.fill(Color.web("#E9E9E9")));
		this.setBorder(Border.stroke(Color.LIGHTGRAY));
	}

	public ImageView getImage() {
		return imageView;
	}

	public void setImage(String image) {
		this.imageView.setImage(new Image(image,IMAGE_WIDTH,IMAGE_HEIGHT,false,false));

	}

	public Text getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading.setText(heading);

	}

	public Text getInformationText() {
		return informationText;
	}

	public void setInformationText(String informationText) {
		this.informationText.setText(informationText);
	}

	public Button getSelectButton() {
		return selectButton;
	}
	

	public void setSelectButton(Button selectButton) {
		this.selectButton = selectButton;

	}
	
	public void setDisableSelectButton(boolean disable) {
		selectButton.setDisable(disable);
		selectButton.setText(disable ? SELECT_BUTTON_TEXT2: SELECT_BUTTON_TEXT1 );
	}

	public BotSelectionView getBotSelectionView() {
		return botSelectionView;
	}

	public void setBotSelectionView(BotSelectionView botSelectionView) {
		this.botSelectionView = botSelectionView;
	}
	
	public Pane getImageWrapper() {
		return imageWrapper;
	}
	
	public ChessPieceColor getBotColor() {
		return selectedColor;
	} 

}
