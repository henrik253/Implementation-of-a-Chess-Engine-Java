module ChessGame {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires deeplearning4j.nn;

	opens main to javafx.graphics, javafx.fxml;
}
