package model.sources;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class Title extends StackPane {

    public Title(String name) {
        Text text = new Text(name);
        text.setStyle("-fx-font-family: Arial; -fx-font-weight: lighter; -fx-font-size: 15; -fx-alignment: center-left");
        getChildren().addAll(text);
    }
}