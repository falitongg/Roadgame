package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public class Camera {
    private double cameraX;
    private final double screenCenter;

    public Camera(double screenCenter) {
        this.screenCenter = screenCenter;
        this.cameraX = 0;
    }

//    public void update(double playerX) {
//        if (playerX > screenCenter) {
//            this.cameraX = playerX - screenCenter;
//        } else {
//            this.cameraX = 0;
//        }
//    }
public void update(double playerX) {
    double targetCameraX = playerX - screenCenter;

    if (targetCameraX > cameraX) {
        cameraX = targetCameraX;
    }

    if (cameraX < 0) {
        cameraX = 0;
    }
}


    public double getX() {
        return cameraX;
    }
}
