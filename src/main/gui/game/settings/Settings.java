package main.gui.game.settings;

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

public class Settings {

	public DoubleProperty boardHeight = new SimpleDoubleProperty(800); 
	public DoubleProperty boardWidth = new SimpleDoubleProperty(800); 
	public ObjectProperty<Color> brightColor = new SimpleObjectProperty<>(Color.web("#f2d4a2"));
	public ObjectProperty<Color> darkColor = new SimpleObjectProperty<>(Color.web("#4a2f01"));
	public StringProperty playButtonText = new SimpleStringProperty("Start Game");

	public int rows = 8; 
	public int columns = 8; 
	public DoubleProperty squareHeight = new SimpleDoubleProperty();
	public DoubleProperty squareWidth = new SimpleDoubleProperty();
	
	public DoubleProperty contentBoxWidthLayoutOffset = new SimpleDoubleProperty(50);
	public DoubleProperty contentBoxHeightLayoutOffset = new SimpleDoubleProperty(50);
	
	public String defaultFENString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public Settings() {
		squareHeight.bind(Bindings.divide(boardHeight, rows));
		squareWidth.bind(Bindings.divide(boardWidth, columns));
	}
}
