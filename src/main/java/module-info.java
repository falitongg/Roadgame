module cz.cvut.fel.pjv.sokolant.roughlikegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens cz.cvut.fel.pjv.sokolant.roughlikegame.view to javafx.graphics;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame;
}