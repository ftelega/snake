module ft.projects.snake {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens ft.projects.snake to javafx.fxml;
    exports ft.projects.snake;
}