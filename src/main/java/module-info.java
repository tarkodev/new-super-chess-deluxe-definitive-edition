module fr.chess.deluxe {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.chess.deluxe to javafx.fxml;
    exports fr.chess.deluxe;
}