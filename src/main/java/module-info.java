module cz.cvut.fel.pjv.sokolant.roughlikegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;


    opens cz.cvut.fel.pjv.sokolant.roughlikegame.view to javafx.graphics;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame.controller;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame.model;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame.util;
    exports cz.cvut.fel.pjv.sokolant.roughlikegame.view;

    opens cz.cvut.fel.pjv.sokolant.roughlikegame.controller to javafx.fxml;
    opens images.bgs to javafx.graphics;
    opens cz.cvut.fel.pjv.sokolant.roughlikegame.data to com.google.gson;


}