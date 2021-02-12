package neutron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        new Lang().start(new Stage());
    }
    public static void en(Stage primaryStage) {
        try {
            Parent root = null;
            System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources\\sample-en.fxml");
            root = FXMLLoader.load(Main.class.getResource("/sample-en.fxml"));
            stage = primaryStage;
            stage.setTitle("Youtube video downloader");
            stage.getIcons().add(new Image("/youtube.jpeg"));
            stage.setResizable(false);
            assert root != null;
            scene = new Scene(root, 600, 650);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void hu(Stage primaryStage) {
        try {
            Parent root = null;
            System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources\\sample-hu.fxml");
            root = FXMLLoader.load(Main.class.getResource("/sample-hu.fxml"));
            stage = primaryStage;
            stage.setTitle("Youtube video downloader");
            stage.getIcons().add(new Image("/youtube.jpeg"));
            stage.setResizable(false);
            assert root != null;
            scene = new Scene(root, 600, 650);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            launch(args);
        }catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
