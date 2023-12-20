package main;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Settings { // Settings is not static, to make different saveable settings possible

	public final int WINDOW_WIDTH = 1200;
	public final int WINDOW_HEIGHT = 800;
	public DoubleProperty boardHeight = new SimpleDoubleProperty(800);
	public DoubleProperty boardWidth = new SimpleDoubleProperty(800);
	public ObjectProperty<Color> brightColor = new SimpleObjectProperty<>(Color.web("#FFFFFF"));
	public ObjectProperty<Color> darkColor = new SimpleObjectProperty<>(Color.web("#4f4f4f"));
	public ObjectProperty<Color> markedColorBright = new SimpleObjectProperty<>(Color.web("#fceaac"));
	public ObjectProperty<Color> markedColorDark = new SimpleObjectProperty<>(Color.web("#ebd173"));
	public ObjectProperty<Color> kingCheckMarked = new SimpleObjectProperty<>(Color.web("#bf291f"));
	public ObjectProperty<Color> moveablePosMarked = new SimpleObjectProperty<>(Color.web("#ebcb59"));
	
	
	public StringProperty playButtonTextStart = new SimpleStringProperty("Start Game");
	public StringProperty playButtonTextContinue = new SimpleStringProperty("Continue");
	public StringProperty inspectGameButtonText = new SimpleStringProperty("inspect");
	public StringProperty inspectGameButtonTextHover = new SimpleStringProperty("inspect (Press Mouse)");
	
	
	public DoubleProperty settingsViewTranslateX = new SimpleDoubleProperty();
	public DoubleProperty settingsViewTranslateY = new SimpleDoubleProperty(0);
	public DoubleProperty settingsViewPrefWidth = new SimpleDoubleProperty();
	public DoubleProperty settingsViewPrefHeight = new SimpleDoubleProperty(WINDOW_HEIGHT);
	public DoubleProperty settingsViewInGamePrefWidth = new SimpleDoubleProperty();
	public DoubleProperty settingsViewInGamePrefHeight = new SimpleDoubleProperty();
	public DoubleProperty settingsViewInGameWidthOffset = new SimpleDoubleProperty(50);
	public DoubleProperty settingsViewInGameButtosOffset = new SimpleDoubleProperty(10);

	public int rows = 8;
	public int columns = 8;
	public DoubleProperty squareHeight = new SimpleDoubleProperty();
	public DoubleProperty squareWidth = new SimpleDoubleProperty();

	public DoubleProperty contentBoxWidthLayoutOffset = new SimpleDoubleProperty();
	public DoubleProperty contentBoxHeightLayoutOffset = new SimpleDoubleProperty();

	// FENS
	public String defaultFENString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	public StringProperty selectedFEN = new SimpleStringProperty(defaultFENString);

	public String[] fenExamples = { "rn2k1r1/ppp1pp1p/3p2p1/5bn1/P7/2N2B2/1PPPPP2/2BNK1RR",
			"3B4/QPNP3K/1P2r3/2p2p2/n2rp3/3P1bk1/8/8 w - - 0 1", "6B1/Ppb1NP2/4pP2/2P5/5p1K/1r4B1/1kp5/5R2 w - - 0 1",
			"5K2/1P2n3/1Bp3bP/1P6/8/3P2N1/R2n3p/2kr2q1 w - - 0 1",
			"8/1b2p3/1p2N1P1/p2p4/7r/Rn1P2K1/P1k5/4rB2 w - - 0 1", "7q/1PP2p1k/1BPPn3/R3np2/p7/4K1Q1/8/N7 w - - 0 1",
			"2N5/4P1K1/4k1B1/2b1p3/3P3p/2pn3N/b3Pr2/3r4 w - - 0 1",
			"2R5/2p5/p1n1b3/q2pb2k/3P2p1/3nK1p1/p6P/8 w - - 0 1", "6q1/7P/2P5/2p2Q2/1p1PPK2/3pP2P/4Ppk1/7R w - - 0 1",
			"B7/3P1qn1/4p2p/b3R3/2R2Np1/P1P3P1/3k4/6K1 w - - 0 1", "4k2r/6r1/8/8/8/8/3R4/R3K3", defaultFENString ,
			"2kr1b1r/2p1qpp1/ppnpbn1p/P3p3/1PB1P3/2N2N2/1BPP1PPP/R2Q1RK1",
			"2kr3r/2p1q1b1/p2ppnp1/Pp6/1P2P2p/2p4P/1BPPQPP1/R3R1K1",
			"k3b3/qp6/2n5/1B6/8/8/3Q1N1P/3K4",
			"1k5b/rrr5/8/8/8/8/RRR5/K6B",
			"3N3q/K3P1BP/2p5/3pPP2/5kr1/1P2R3/B6n/8 w - - 0 1",
			"3r2r1/q1P2K2/7b/1N5p/2pP3B/7p/p2n3k/5b2 w - - 0 1",
			"7n/2B1K2p/q1Pr4/R1n1Qp2/8/8/1rNk2BP/8 w - - 0 1",
			"8/P6p/1k6/1P1K3b/1N5Q/r5PN/2p1n1B1/3R3R w - - 0 1",
			"8/N5pp/1P2QP1n/P3P1p1/k2p4/3R1P2/2K2P2/8 w - - 0 1",
			"2b1K3/1q4p1/6Pp/1N2Pp2/2N5/1P3r2/P1k1n2P/8 w - - 0 1",
			"8/1nRr1k2/p6K/P2PP3/2N1pP2/b6P/5P2/1q6 w - - 0 1",
			"2bn1k1N/4b3/3rP3/8/1P6/B2p4/P2P2pK/1r3q2"
	};



	public Settings() {
		squareHeight.bind(Bindings.divide(boardHeight, rows));
		squareWidth.bind(Bindings.divide(boardWidth, columns));
		contentBoxWidthLayoutOffset.bind(Bindings.divide(boardHeight, 16));
		contentBoxHeightLayoutOffset.bind(Bindings.divide(boardHeight, 16));
		settingsViewTranslateX.bind(boardWidth);
		settingsViewPrefWidth.bind(Bindings.subtract(WINDOW_WIDTH, boardWidth));
		settingsViewInGamePrefWidth.bind(settingsViewPrefWidth.subtract(settingsViewInGameWidthOffset));
//		settingsViewPrefHeight.bind(WINDOW_Height);

	}
}
