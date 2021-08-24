package com.neutron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static Stage stage;
    public static Shared s;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = null;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample.fxml")));
        stage = primaryStage;
        stage.setTitle("Youtube videó letöltő");
        //stage.getIcons().add(new Image("/youtube2.png"));
        stage.setResizable(false);
        assert root != null;
        stage.setScene(new Scene(root, 600, 650));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
    public static void err(Throwable th) {
        // TODO Write error to file
    }
}
