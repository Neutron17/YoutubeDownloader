package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = null;
        System.out.println(System.getProperty("user.dir")+"\\src\\main\\resources\\sample.fxml");
        root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage = primaryStage;
        stage.setTitle("Youtube videó letöltő");
        stage.getIcons().add(new Image("/youtube.jpeg"));
        stage.setResizable(false);
        assert root != null;
        stage.setScene(new Scene(root, 600, 650));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
