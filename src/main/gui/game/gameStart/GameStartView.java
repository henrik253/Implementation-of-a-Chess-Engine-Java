package main.gui.game.gameStart;


import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.gui.game.Overlay;
import main.gui.game.settings.Settings;


public class GameStartView extends Pane {

	private GameStartPresenter gameStartPresenter;	
	private Settings settings;
	private Overlay overlay;
	
	private Pane contentBox;
	private Button play;
	
	public void init() {
		
		initContentBox(); 
		initPlayButton();
		
		
		initIDs();
		style(); 
	}
	

	private void initContentBox() {
		contentBox = new Pane();
		contentBox.setPrefHeight(settings.boardHeight.get()/4 + settings.contentBoxHeightLayoutOffset.get());
		contentBox.setPrefWidth(settings.boardWidth.get()/2 + settings.contentBoxWidthLayoutOffset.get());
		contentBox.setTranslateX( (settings.boardWidth.get()/2) - (contentBox.getPrefWidth()/2));
		contentBox.setTranslateY( (settings.boardHeight.get()/2) - (contentBox.getPrefHeight()/2));
		contentBox.setId("contentBox");
	}
	
	private void initPlayButton() {
		play = new Button(settings.playButtonText.get());
		play.setPrefHeight(contentBox.getPrefHeight() / 5);
		play.setPrefWidth((contentBox.getPrefWidth()/5) * 4);
		play.setTranslateX( (contentBox.getPrefWidth() - play.getPrefWidth())/2 );
		play.setTranslateY((contentBox.getPrefHeight() / 4) * 3);
		play.setOnMousePressed(this::handle);
	}
	
	private void handle(MouseEvent event) {
		gameStartPresenter.playButtonPressed();
	}
	
	private void initPlayerInfo() {
		
	}
	
	private void initIDs() {
		play.setId("playButton");
	}
	
	private void style() {
		this.getChildren().add(overlay);  // Adding the color fade in the background
		this.getChildren().add(contentBox);
		contentBox.getChildren().add(play);
	}
	
	public GameStartPresenter getGameStartPresenter() {
		return gameStartPresenter;
	}

	public void setGameStartPresenter(GameStartPresenter gameStartPresenter) {
		this.gameStartPresenter = gameStartPresenter;
	}
	

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}
	

}
