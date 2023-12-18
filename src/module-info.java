module ChessGame {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires junit;
//    requires deeplearning4j.nn;
//	requires nd4j.api;
//	requires deeplearning4j.nn;
//	requires nd4j.api;

    opens main to javafx.graphics;
    opens test.main.model to junit;
}
