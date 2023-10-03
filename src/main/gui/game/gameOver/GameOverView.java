package main.gui.game.gameOver;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Settings;
import main.gui.game.Overlay;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;

public class GameOverView extends Pane {

	private static final String PATH = "file:resources/";
	private static final String FILENAME = "user";
	private static final String DATATYPE = ".png";
	private static final Double IMAGE_SIZE = 64.0;

	private static final String HEADING_TEXT = "0 : 0";
	private static final Double HEADING_SIZE = 23.0;

	private static final String PLAYER_NAME = "User";

	private static final String BLACK = "#403f3f";

	private static final String GAME_SENTENCE = "YOU WON";

	private GameOverPresenter gameOverPresenter;
	private Settings settings;
	private Overlay overlay;

	private Pane contentBox;
	private Button playAgainButton;

	private BorderPane playerWrapper;
	private Pane playerImageWrapper;
	private Pane playerHeadingWrapper;
	private Text playerHeading;

	private BorderPane textWrapper;

	private BorderPane botWrapper;
	private Pane botImageWrapper;
	private Pane botHeadingWrapper;
	private Text botHeading;

	private Text vsHeading;

	private BorderPane gameSentenceWrapper;
	private Text gameSentence;

	private ImageView playerImage = new ImageView(
			new Image(PATH + FILENAME + DATATYPE, IMAGE_SIZE, IMAGE_SIZE, false, false));
	private ImageView botImage;

	public void init() {
		initContentBox();
		initPlayButton();

		initVsTextWrapper(); // PlayerWrapper & BotWrapper elems need the TextWrapper to be first init.
		initBotWrapper();
		initPlayerWrapper();
		initGameSentenceWrapper();

		style();
	}

	private void style() {
		this.getChildren().add(overlay); // Adding the color fade in the background
		this.getChildren().add(contentBox);
		contentBox.getChildren().addAll(playAgainButton, playerWrapper, textWrapper, botWrapper, gameSentenceWrapper);
	}

	private void initContentBox() {
		contentBox = new Pane();
		contentBox.setPrefHeight(settings.boardHeight.get() / 4 + settings.contentBoxHeightLayoutOffset.get());
		contentBox.setPrefWidth(settings.boardWidth.get() / 2 + settings.contentBoxWidthLayoutOffset.get());
		contentBox.setTranslateX((settings.boardWidth.get() / 2) - (contentBox.getPrefWidth() / 2));
		contentBox.setTranslateY((settings.boardHeight.get() / 2) - (contentBox.getPrefHeight() / 2));
		contentBox.setId("contentBox");
	}

	private void initPlayButton() {
		playAgainButton = new Button(settings.playButtonTextContinue.get());
		playAgainButton.setPrefHeight(contentBox.getPrefHeight() / 5);
		playAgainButton.setPrefWidth((contentBox.getPrefWidth() / 5) * 4);
		playAgainButton.setTranslateX((contentBox.getPrefWidth() - playAgainButton.getPrefWidth()) / 2);
		playAgainButton.setTranslateY((contentBox.getPrefHeight() / 4) * 3);
		playAgainButton.setOnMousePressed(event -> gameOverPresenter.playAgainButtonPressed());
		playAgainButton.setId("playButton");
	}

	private void initBotWrapper() { // X = Right
		botWrapper = new BorderPane();
		botImageWrapper = new Pane();
		botHeadingWrapper = new Pane();
		botHeading = new Text("");

		double y = textWrapper.getTranslateY() - IMAGE_SIZE / 2;
		double x = (contentBox.getPrefWidth() / 4) * 3 - IMAGE_SIZE / 2 - botHeading.getBoundsInLocal().getWidth();
		botWrapper.setTranslateY(y);
		botWrapper.setTranslateX(x);

		botImageWrapper.setId("gameStartBotWrapper");

		double offset = botHeading.getBoundsInLocal().getHeight() * 2;
		botHeadingWrapper.setTranslateY(y + IMAGE_SIZE + offset);
		botHeadingWrapper.setTranslateX(x);
		botHeading.setId("gameStartBotHeading");

		botImageWrapper.setMaxWidth(IMAGE_SIZE);

		botWrapper.setCenter(botImageWrapper);
		botHeadingWrapper.getChildren().add(botHeading);

		contentBox.getChildren().add(botHeadingWrapper);
	}

	private void initPlayerWrapper() { // X = Left
		playerWrapper = new BorderPane();
		playerImageWrapper = new Pane();
		playerHeading = new Text(PLAYER_NAME);
		playerHeadingWrapper = new Pane();

		playerHeading.setId("gameStartPlayerHeading");

		double y = textWrapper.getTranslateY() - IMAGE_SIZE / 2;
		double x = (contentBox.getPrefWidth() / 4) * 1 - IMAGE_SIZE / 2;

		playerWrapper.setTranslateY(y);
		playerWrapper.setTranslateX(x);
		playerImageWrapper.setId("gameStartPlayerWrapper");

		playerImageWrapper.getChildren().add(playerImage); // player Image already init.
		playerImageWrapper.setMaxWidth(IMAGE_SIZE);
		playerWrapper.setCenter(playerImageWrapper);

		playerHeadingWrapper.getChildren().add(playerHeading);
		double offset = playerHeading.getBoundsInLocal().getHeight() * 2;
		playerHeadingWrapper.setTranslateY(y + IMAGE_SIZE + offset);
		playerHeadingWrapper.setTranslateX(x);

		playerHeading.setId("gameStartPlayerHeading");

		contentBox.getChildren().add(playerHeadingWrapper);
	}

	private void initVsTextWrapper() { // X and Y are Centered
		vsHeading = new Text(HEADING_TEXT);
		vsHeading.setId("gameStartHeading");

		textWrapper = new BorderPane();

		textWrapper.setId("gameStartTextWrapper");

		double offset = vsHeading.getBoundsInLocal().getWidth();
		textWrapper.setTop(vsHeading);
		textWrapper.setTranslateX(playAgainButton.getPrefWidth() / 2 + offset);
		textWrapper.setTranslateY((contentBox.getPrefHeight() / 5) * 2);
	}

	private void initGameSentenceWrapper() {
		gameSentenceWrapper = new BorderPane();
		gameSentence = new Text(GAME_SENTENCE);
		gameSentence.setId("GameSentence");

		gameSentenceWrapper.setTop(gameSentence);
		double offset = gameSentence.getBoundsInLocal().getWidth() / 2;
		gameSentenceWrapper.setTranslateX(playAgainButton.getPrefWidth() / 2 + offset);
		gameSentenceWrapper.setTranslateY((contentBox.getPrefHeight() / 4));
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public GameOverPresenter getGameOverPresenter() {
		return gameOverPresenter;
	}

	public void setGameOverPresenter(GameOverPresenter gameOverPresenter) {
		this.gameOverPresenter = gameOverPresenter;
	}

	public void drawSelectedBot(BotRepresentation source) {
		this.botImage = new ImageView(source.getImage().getImage());
		botWrapper.setBackground(Background.fill(source.getUserColor().isWhite() ? Color.web(BLACK) : Color.WHITE));
		playerWrapper.setBackground(Background.fill(source.getUserColor().isWhite() ? Color.WHITE : Color.web(BLACK)));

		drawBotRepresentation();
		drawBotName(source);
	}

	private void drawBotRepresentation() {
		this.botImageWrapper.getChildren().clear();
		this.botImageWrapper.getChildren().add(botImage);

	}

	private void drawBotName(BotRepresentation source) {
		botHeading.setText(source.getHeading().getText());
	}

	public void drawScore(int userWon, int botWon) {
		vsHeading.setText(userWon + " : " + botWon);
	}

}
