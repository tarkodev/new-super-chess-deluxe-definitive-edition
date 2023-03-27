module fr.chess.deluxe {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.chess.deluxe to javafx.fxml;
    exports fr.chess.deluxe;
    exports fr.chess.deluxe.piece;
    opens fr.chess.deluxe.piece to javafx.fxml;
    exports fr.chess.deluxe.utils;
    opens fr.chess.deluxe.utils to javafx.fxml;
}