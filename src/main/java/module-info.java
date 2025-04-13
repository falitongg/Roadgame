module cz.cvut.fel.pjv.sokolant.roughlikegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens cz.cvut.fel.pjv.sokolant.roughlikegame.view to javafx.graphics;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

    opens cz.cvut.fel.pjv.sokolant.roughlikegame.controller to javafx.fxml;

}