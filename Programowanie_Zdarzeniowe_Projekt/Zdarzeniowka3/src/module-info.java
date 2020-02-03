module mp3player {
    requires javafx.graphics;       //zależność od modułu wyswietlania
    requires javafx.controls;       //zależność od modułu fxml
    requires javafx.fxml;           //zależność od modułu fxml
    requires javafx.media;
    requires jid3lib;

    exports pl.javastart.mp3player.main to javafx.graphics;     //export głównego pakietu apki do modułu
    opens pl.javastart.mp3player.controller to javafx.fxml;
    opens pl.javastart.mp3player.mp3 to javafx.base;
}