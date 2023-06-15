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
import main.gui.game.settings.SettingsPresenter;
import main.gui.game.settings.SettingsView;
import main.model.Model;
import main.model.gameLogic.GameLogic;
import main.model.gameLogic.MoveValidation;

public class Main extends Application {

	private Scene scene;

	private MainPresenter mainPresenter;
	private SettingsPresenter settingsPresenter;
	private GamePresenter gamePresenter;
	private GameOverPresenter gameOverPresenter;
	private GameStartPresenter gameStartPresenter;

	private MainView mainView;
	private GameView gameView;
	private SettingsView settingsView;
	private GameOverView gameOverView;
	private GameStartView gameStartView;

	private Settings settings;
	private Board board;
	private Overlay overlay;

	private Model model; 
	private MoveValidation moveValidation;
	private GameLogic gameLogic; 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initGUIComponents();
		initModelComponents();
		combineGUItoModel();

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void initGUIComponents() {

		// All Presenters
		mainPresenter = new MainPresenter();

		gamePresenter = new GamePresenter();
		settingsPresenter = new SettingsPresenter();
		gameOverPresenter = new GameOverPresenter();
		gameStartPresenter = new GameStartPresenter();

		// All Views
		mainView = new MainView();
		gameView = new GameView();
		settingsView = new SettingsView();
		gameOverView = new GameOverView();
		gameStartView = new GameStartView();

		// All other Class
		settings = new Settings();
		board = new Board();
		overlay = new Overlay();

		// Setting all Classes

		mainPresenter.setGamePresenter(gamePresenter);
		mainPresenter.setGameStartPresenter(gameStartPresenter);
		mainPresenter.setSettingsPresenter(settingsPresenter);
		mainPresenter.setMainView(mainView);
		mainPresenter.setSettings(settings);

		gamePresenter.setGameView(gameView);
		gamePresenter.setMainPresenter(mainPresenter);

		settingsPresenter.setSettings(settings);
		settingsPresenter.setSettingsView(settingsView);
		settingsPresenter.setMainPresenter(mainPresenter);

		gameOverPresenter.setGameOverView(gameOverView);
		gameOverPresenter.setGamePresenter(gamePresenter);

		gameStartPresenter.setGameStartView(gameStartView);
		gameStartPresenter.setMainPresenter(mainPresenter);
		gameStartPresenter.setSettings(settings);

		mainView.setGameOverView(gameOverView);
		mainView.setGameStartView(gameStartView);
		mainView.setGameView(gameView);
		mainView.setMainPresenter(mainPresenter);
		mainView.setSettingsView(settingsView);
		mainView.setSettings(settings);

		gameView.setGamePresenter(gamePresenter);
		gameView.setBoard(board);

		gameStartView.setGameStartPresenter(gameStartPresenter);
		gameStartView.setSettings(settings);
		gameStartView.setOverlay(overlay);

		gameOverView.setGameOverPresenter(gameOverPresenter);
		gameOverView.setSettings(settings);
		gameOverView.setOverlay(overlay);

		settingsView.setSettings(settings);
		settingsView.setSettingsPresenter(settingsPresenter);

		board.setSettings(settings);
		board.setGameView(gameView);

		overlay.setSettings(settings);

		// Init other classes
		overlay.init();

		// Init all Views
		mainView.init();
		gameStartView.init();
		gameOverView.init();
		settingsView.init();

		// Execute
		// Board
		board.drawBoard();
		mainPresenter.loadBoard(settings.defaultFENString);
		String fen = getRandomFEN();
		settings.selectedFEN.set(settings.defaultFENString);
		mainPresenter.loadBoard(settings.selectedFEN.get());
		

		// Scene
		scene = new Scene(mainView, settings.WINDOW_WIDTH, settings.WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}

	public void initModelComponents() {
		model = new Model();
		moveValidation = new MoveValidation(); 
		model.setSettings(settings);
		model.setMoveValidation(moveValidation);
		moveValidation.setModel(model);
	
	}

	public void combineGUItoModel() {
		mainPresenter.setModel(model);
		model.setMainPresenter(mainPresenter);
	}

	public String getRandomFEN() {
		return settings.fenExamples[(int) (Math.random() * settings.fenExamples.length)];
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
