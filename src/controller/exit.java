package controller;

import javafx.application.Platform;

public class exit {

    public void exit(){
        System.out.println("Exit");
        Platform.exit();
        System.exit(0);
    }
}
