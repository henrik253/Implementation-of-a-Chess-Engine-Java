package main.gui.game.sidebar.botrepresentations;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ChessPieceColor;

public class BotRepresentation extends Pane {

	private String name;

	private static final int FONT_SIZE = 16;

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

	private ChessPieceColor userPlaysAs;

	private VBox colorSelectMenue = new VBox();

	private Text selectHeading = new Text("Choose your color");

	private HBox buttonWrapper = new HBox();
	private Button selectWhiteButton = new Button();
	private Button selectBlackButton = new Button();
	private Button selectRandButton = new Button("?");

	private VBox sliderWrapper = new VBox(1);
	private Slider slider = new Slider(0, 10, 1);
	private String format = " ms."; // by default ms.
	private Label sliderLabel = new Label( ((int) slider.getValue()) + format);
	

	public BotRepresentation(BotSelectionView botSelectionView, String name) {
		this.botSelectionView = botSelectionView;
		userPlaysAs = ChessPieceColor.WHITE; // By default black
		colorButtonPressed(selectWhiteButton); // By default white is selected
		this.name = name;
		init();
	}

	private void init() { // TODO Select button is going to change to surr & Remi B. when game is Running
		this.setId("BotRepresentationMenue");
		this.setTranslateY(OFFSET * 2);
		this.setTranslateX(OFFSET + OFFSET / 2);
		this.setPrefSize(WINDOW_WIDTH, 300);

		initImageWrapper();
		initHeading();
		initInformationText();

		initSurrenderButton();
		initSelectButton();

		initColorSelectMenue();
		initSlider();
		this.setBackground(new Background(new BackgroundFill(Color.web("#E9E9E9"), null, null)));
		this.setBorder(
				new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

		imageWrapper.getChildren().add(imageView);
		this.getChildren().addAll(selectButton, informationText, sliderWrapper, heading, imageWrapper);
	}

	private void initImageWrapper() {
		this.imageWrapper.setTranslateY(-OFFSET);
		this.imageWrapper.setTranslateX(-OFFSET);

	}

	private void initHeading() {
		this.heading.setTranslateX(IMAGE_WIDTH / 2 + IMAGE_HEADING_OFFSET);
		this.heading.setTranslateY(IMAGE_HEIGHT / 2);
		this.heading.setId("BotHeadingText");
	}

	private void initInformationText() {
		this.informationText.setTranslateY(IMAGE_HEIGHT + INFO_TEXT_OFFSET);
		this.informationText.setTranslateX(PADDING);
		this.informationText.setWrappingWidth(WINDOW_WIDTH - OFFSET);
		this.informationText.setFont(new Font(FONT_SIZE));
	}

	private void initSelectButton() {
		this.selectButton.setTranslateY(this.getPrefHeight() - OFFSET * 2);
		this.selectButton.setMinWidth(WINDOW_WIDTH / 2);
		this.selectButton.setTranslateX(WINDOW_WIDTH / 4);
		this.selectButton.setId("BotSelectButton");

		selectButton.setOnAction(event -> {
			botSelectionView.selectedPressed(this);
		});
	}

	private void initSurrenderButton() {
		surrenderButton.setTranslateY(getPrefHeight() - OFFSET * 2);
		surrenderButton.setMinWidth(WINDOW_WIDTH / 2);
		surrenderButton.setTranslateX(WINDOW_WIDTH / 4);
		surrenderButton.setId("BotSurrenderButton");

		surrenderButton.setOnAction(event -> {
			botSelectionView.surrenderPressed();
		});
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
		selectHeading.setFont(new Font(FONT_SIZE));
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
				this.userPlaysAs = (ChessPieceColor) userData;
			} else {
				this.userPlaysAs = getRandomColor();

			}

		});
	}

	private ChessPieceColor getRandomColor() {
		return Math.random() < PROBABILLITY ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
	}

	public void toggleSelectButton() {
		getChildren().remove(selectButton);
//		selectButton.setVisible(false);
		if (!getChildren().contains(surrenderButton)) {
			getChildren().add(surrenderButton);
		}
//		surrenderButton.setVisible(true);
	}

	public void toggleSurrenderButton() {
		Platform.runLater(() -> getChildren().remove(surrenderButton));

		if (!getChildren().contains(selectButton)) {
			getChildren().add(selectButton);
		}

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

	public ChessPieceColor getUserColor() {
		return userPlaysAs;
	}

	public void disableColorSelect(boolean disable) {
		selectWhiteButton.setDisable(disable);
		selectBlackButton.setDisable(disable);
		selectRandButton.setDisable(disable);
	}

	public String getName() {
		return name;
	}

	private void initSlider() {
		sliderWrapper.setTranslateX(this.getPrefWidth() / 20 + 25);
		sliderWrapper.setTranslateY(this.getPrefHeight() / 3 - 10);
		slider.setShowTickLabels(false);
		slider.setShowTickMarks(true);
		slider.setPrefWidth( (this.getPrefWidth() / 4) * 3);
		// Create a Label
		//sliderWrapper.setAlignment(Pos.CENTER);
		// Add a ChangeListener to the Slider
		slider.valueProperty().addListener((obs, oldV, newV) -> {
			sliderLabel.setText("" + newV.intValue() + " " +  format);
		});
		sliderLabel.textProperty().addListener( (obs,old,newV) -> {
			int textLength = newV.toString().length();
			sliderLabel.setTranslateX(slider.getPrefWidth() / 2 - 25 - 2*textLength);
		});
		sliderLabel.setTranslateX(slider.getPrefWidth() / 2 - 25);
		sliderLabel.setFont(new Font(FONT_SIZE));
		
		sliderWrapper.getChildren().addAll(slider, sliderLabel);
	}
	
	public void setSliderMinMax(int min,int max,int defaultValue) {
		slider.setMin(min);
		slider.setMax(max);
		slider.setValue(defaultValue);
	}
	
	public void setFormatForSlider(String format) {
		this.format = format;
		sliderLabel.setText(((int) slider.getValue()) + format);
	}

	public Slider getSlider() {
		return slider;
	}

}
