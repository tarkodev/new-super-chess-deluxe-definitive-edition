module fr.chess.deluxe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports fr.chess.deluxe;
    opens fr.chess.deluxe to javafx.controls, javafx.fxml, com.google.gson;

    exports fr.chess.deluxe.piece;
    opens fr.chess.deluxe.piece to  javafx.controls, javafx.fxml, com.google.gson;

    exports fr.chess.deluxe.utils;
    opens fr.chess.deluxe.utils to  javafx.controls, javafx.fxml, com.google.gson;

    exports fr.chess.deluxe.movement;
    opens fr.chess.deluxe.movement to  javafx.controls, javafx.fxml, com.google.gson;
}