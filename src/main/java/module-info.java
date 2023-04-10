module fr.chess.deluxe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens fr.chess.deluxe to javafx.fxml, com.google.gson;
    exports fr.chess.deluxe;
    exports fr.chess.deluxe.piece;
    opens fr.chess.deluxe.piece to javafx.fxml, com.google.gson;
    exports fr.chess.deluxe.utils;
    opens fr.chess.deluxe.utils to javafx.fxml, com.google.gson;
	exports fr.chess.deluxe.movement;
	opens fr.chess.deluxe.movement to javafx.fxml, com.google.gson;
}