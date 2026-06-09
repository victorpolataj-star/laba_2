package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game_view.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Лови шарик");
        primaryStage.setScene(new Scene(root, 850, 700));

        primaryStage.setOnCloseRequest(event -> {
            GameController controller = loader.getController();
            if (controller != null) controller.stopGame();
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}