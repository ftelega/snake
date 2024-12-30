package ft.projects.snake;

import ft.projects.snake.vc.GameViewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static ft.projects.snake.util.Constants.*;

public class SnakeApplication extends Application {

    @Override
    public void start(Stage stage) {
        var gameVC = new GameViewController();
        Scene scene = new Scene(gameVC.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(gameVC.new GameViewListener());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            System.exit(1);
        });
        stage.show();
        gameVC.startGame();
    }

    public static void main(String[] args) {
        launch();
    }
}