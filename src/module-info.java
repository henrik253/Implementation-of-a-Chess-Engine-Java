module ChessGame {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	
	opens main to javafx.graphics, javafx.fxml;
}
