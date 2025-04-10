module cz.cvut.fel.pjv.sokolant.roughlikegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.cvut.fel.pjv.sokolant.roughlikegame to javafx.fxml;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame;
}