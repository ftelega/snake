package ft.projects.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SnakeApplication extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new VBox(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}