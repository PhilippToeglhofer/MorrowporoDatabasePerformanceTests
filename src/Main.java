import javafx.application.Application;
import javafx.stage.Stage;
import view.MenuWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Create GUI ...");
        new MenuWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    //will only be called if you exit the application
    public void stop(){
        System.out.println("System stopped");
        System.exit(0);
    }
}
