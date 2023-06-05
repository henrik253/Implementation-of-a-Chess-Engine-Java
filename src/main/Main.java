package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.gui.MainPresenter;
import main.gui.MainView;
import main.gui.game.Overlay;
import main.gui.game.board.Board;
import main.gui.game.board.GamePresenter;
import main.gui.game.board.GameView;
import main.gui.game.gameOver.GameOverPresenter;
import main.gui.game.gameOver.GameOverView;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.gameStart.GameStartView;
import main.gui.game.settings.Settings;
import main.gui.game.settings.SettingsPresenter;
import main.gui.game.settings.SettingsView;

public class Main extends Application {
	private static final int WINDOW_WIDTH = 1200;
	private static final int WINDOW_HEIGHT = 800;
	
	private Scene scene;
	
	private MainPresenter mainPresenter;
	private GamePresenter gamePresenter;
	private GameOverPresenter gameOverPresenter;
	private GameStartPresenter gameStartPresenter;

	private MainView mainView;
	private GameView gameView;
	private SettingsView settingsView;
	private GameOverView gameOverView;
	private GameStartView gameStartView;

	@Override
	public void start(Stage primaryStage) throws Exception {
		initGUIComponents();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void initGUIComponents() {

		
		
		// All Presenters
		MainPresenter mainPresenter = new MainPresenter();

		GamePresenter gamePresenter = new GamePresenter();
		SettingsPresenter settingsPresenter = new SettingsPresenter();
		GameOverPresenter gameOverPresenter = new GameOverPresenter();
		GameStartPresenter gameStartPresenter = new GameStartPresenter();

		// All Views
		MainView mainView = new MainView();
		GameView gameView = new GameView();
		SettingsView settingsView = new SettingsView();
		GameOverView gameOverView = new GameOverView();
		GameStartView gameStartView = new GameStartView();

		// All other Class
		Settings settings = new Settings();
		Board board = new Board(); 
		Overlay overlay = new Overlay();
		
		// Setting all Classes

		mainPresenter.setGamePresenter(gamePresenter);
		mainPresenter.setGameStartPresenter(gameStartPresenter);
		mainPresenter.setSettingsPresenter(settingsPresenter);
		mainPresenter.setMainView(mainView);

		gamePresenter.setGameView(gameView);
		gamePresenter.setMainPresenter(mainPresenter);

		settingsPresenter.setSettings(settings);
		settingsPresenter.setSettingsView(settingsView);

		gameOverPresenter.setGameOverView(gameOverView);
		gameOverPresenter.setGamePresenter(gamePresenter);

		gameStartPresenter.setGameStartView(gameStartView);
		gameStartPresenter.setMainPresenter(mainPresenter);

		mainView.setGameOverView(gameOverView);
		mainView.setGameStartView(gameStartView);
		mainView.setGameView(gameView);
		mainView.setMainPresenter(mainPresenter);
		mainView.setSettingsView(settingsView);
		mainView.setSettings(settings);
		
		
		gameView.setGamePresenter(gamePresenter);
		gameView.setBoard(board);
		
		gameStartView.setGameStartPresenter(gameStartPresenter);

		gameOverView.setGameOverPresenter(gameOverPresenter);
		
		board.setSettings(settings);
		board.setGameView(gameView);
		
		gameStartView.setSettings(settings);
		
		gameOverView.setSettings(settings);
		
		gameStartView.setOverlay(overlay);
		
		gameOverView.setOverlay(overlay);
		
		overlay.setSettings(settings);
		
		//Init other classes 
		overlay.init();
		//Init all Views
		mainView.init();
		gameStartView.init();
		gameOverView.init();
		settingsView.init();
		
		// Execute 
		board.draw(); 
		mainPresenter.loadBoard(settings.defaultFENString);
		
		scene = new Scene(mainView,WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
	}
	


	public static void main(String[] args) {
		launch(args);
	}

}
