package main.gui.game.settings.settingsViewComponents;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
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
	private static final Double INFO_TEXT_OFFSET = 10.0;
	private static final Double WINDOW_WIDTH = 400 - OFFSET * 2;

	private static final String SELECT_BUTTON_TEXT1 = "select";
	private static final String SELECT_BUTTON_TEXT2 = "selected";

	private static final Double BUTTON_SPACING = 10.0;
	private static final Double BUTTON_WIDTH = 50.0;
	private static final Double BUTTON_HEIGHT = 30.0;

	private static final Double PROBABILLITY = 0.5;
	
	private Pane imageWrapper = new Pane();
	private ImageView imageView = new ImageView();
	private Text heading = new Text("Heading");
	private Text informationText = new Text("");

	private Button selectButton = new Button("Select");
	private Button surrenderButton = new Button("surrender");
	private Button remisButton = new Button("Remis");

	private BotSelectionView botSelectionView;

	private ChessPieceColor selectedColor;

	private VBox colorSelectMenue = new VBox();

	private Text selectHeading = new Text("Choose your color");

	private HBox buttonWrapper = new HBox();
	private Button selectWhiteButton = new Button();
	private Button selectBlackButton = new Button();
	private Button selectRandButton = new Button("?");

	public BotRepresentation(BotSelectionView botSelectionView) {
		this.botSelectionView = botSelectionView;
		selectedColor = ChessPieceColor.WHITE; // By default black
		colorButtonPressed(selectWhiteButton); // By default white is selected
		init();

	}

	private void init() { // TODO Select button is going to change to surr & Remi B. when game is Running
		this.setId("BotRepresentationMenue");
		this.setTranslateY(OFFSET * 2);
		this.setTranslateX(OFFSET + OFFSET / 2);
		this.setPrefSize(WINDOW_WIDTH, 300);

		imageWrapper.getChildren().add(this.imageView);
		this.imageWrapper.setTranslateY(-OFFSET);
		this.imageWrapper.setTranslateX(-OFFSET);
		this.getChildren().add(imageWrapper);

		this.getChildren().add(heading);
		this.heading.setTranslateX(IMAGE_WIDTH / 2 + IMAGE_HEADING_OFFSET);
		this.heading.setTranslateY(IMAGE_HEIGHT / 2);
		this.heading.setId("BotHeadingText");

		this.getChildren().add(informationText);

		this.informationText.setTranslateY(IMAGE_HEIGHT + INFO_TEXT_OFFSET);
		this.informationText.setTranslateX(PADDING);
		this.informationText.setWrappingWidth(WINDOW_WIDTH - OFFSET);

		this.getChildren().add(selectButton);

		this.selectButton.setTranslateY(this.getPrefHeight() - OFFSET * 2);
		this.selectButton.setMinWidth(WINDOW_WIDTH / 2);
		this.selectButton.setTranslateX(WINDOW_WIDTH / 4);
		this.selectButton.setId("BotSelectButton");

		selectButton.setOnAction(event -> {
			botSelectionView.selectedPressed(this);
		});

		initColorSelectMenue();

		this.setBackground(Background.fill(Color.web("#E9E9E9")));
		this.setBorder(Border.stroke(Color.LIGHTGRAY));
	}

	private void initColorSelectMenue() {
		selectWhiteButton.setId("SelectWhiteButton");
		selectWhiteButton.setUserData(ChessPieceColor.WHITE);
		selectBlackButton.setId("SelectBlackButton");
		selectBlackButton.setUserData(ChessPieceColor.BLACK);
		selectRandButton.setId("SelectRandButton");

		buttonWrapper.getChildren().addAll(selectWhiteButton, selectRandButton, selectBlackButton);
		buttonWrapper.getChildren().forEach(button -> {
			Button b = (Button) button;
			b.setPrefWidth(BUTTON_WIDTH);
			b.setPrefHeight(BUTTON_HEIGHT);
			b.setOnAction(e -> colorButtonPressed((Button) e.getSource()));
		});

		buttonWrapper.setSpacing(BUTTON_SPACING);
		colorSelectMenue.getChildren().addAll(selectHeading, buttonWrapper);
		this.getChildren().add(colorSelectMenue);

		colorSelectMenue.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
			colorSelectMenue.setTranslateX((this.getPrefWidth() - newValue.getWidth()) / 2);
			colorSelectMenue.setTranslateY(this.getPrefHeight() / 2);
		});

	}

	private void colorButtonPressed(Button source) {
		buttonWrapper.getChildren().forEach(button -> {
			Button b = (Button) button;
			if (b != source)
				b.setDisable(false);
			else
				b.setDisable(true);

			setDisableSelectButton(false);
			
			Object userData = source.getUserData();
			if (userData != null) {
				this.selectedColor = (ChessPieceColor) userData;
			} else {
				this.selectedColor = getRandomColor();
				
			}

		});
	}
	
	private ChessPieceColor getRandomColor() {
		return Math.random() < PROBABILLITY ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
	}

	public ImageView getImage() {
		return imageView;
	}

	public void setImage(String image) {
		this.imageView.setImage(new Image(image, IMAGE_WIDTH, IMAGE_HEIGHT, false, false));

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
		selectButton.setText(disable ? SELECT_BUTTON_TEXT2 : SELECT_BUTTON_TEXT1);
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

	public ChessPieceColor getSelectedColor() {
		return selectedColor;
	}

}
