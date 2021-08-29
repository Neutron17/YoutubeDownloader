package com.neutron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    public static Shared s;
    public static DB db;
    @Override
    public void start(Stage primaryStage) throws Exception {
        db = new DB();
        System.out.println(db.getAllElements());
        Parent root = null;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample.fxml")));
        stage = primaryStage;
        stage.setTitle(s.ser.getLanguage().getTitle());
        stage.getIcons().add(new Image("/youtube.jpeg"));
        stage.setResizable(false);
        assert root != null;
        scene = new Scene(root, 600, 650);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
    public static void err(Throwable th) {
        // TODO Write error to file
    }
}
