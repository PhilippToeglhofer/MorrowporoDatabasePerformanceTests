package view;

import controller.Controller;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuWindow {

    private final Controller controller;
    private final Stage primaryStage;
    private final Scene primaryScene;

    public MenuWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        double windowWidth = 1280;
        double windowHeight = 720;
        String versionNumber = "1.0.0";
        System.out.println("Create Controller ...");
        controller = new Controller(this, windowWidth, windowHeight);
        System.out.println("Create Scene ...");
        this.primaryScene = new Scene(controller.createStartMenu(), windowWidth, windowHeight);
        this.primaryStage.setScene(primaryScene);
        this.primaryStage.setTitle("Morrow Database (" + versionNumber + ")");
        //this.primaryStage.getIcons().add(new Image("Model/Images/xyz.png"));
        this.primaryStage.show();
        System.out.println("Display GUI ...");
    }

    void enableFullscreen(){
        primaryStage.setFullScreen(true);
    }

    void disableFullscreen(){
        primaryStage.setFullScreen(false);
    }

    Scene getPrimaryScene() {
        return primaryScene;
    }
}

