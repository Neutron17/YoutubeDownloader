package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Lang extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(350);
        primaryStage.setHeight(270);
        primaryStage.setResizable(false);
        AnchorPane pane = new AnchorPane();
        Button submit = new Button();
        submit.setText("SUBMIT");
        submit.setLayoutY(200);
        AnchorPane.setLeftAnchor(submit,0.0);
        AnchorPane.setRightAnchor(submit,0.0);

        ToggleButton en = new ToggleButton();
        ToggleButton hu = new ToggleButton();
        ToggleGroup lang = new ToggleGroup();
        en.setToggleGroup(lang);
        hu.setToggleGroup(lang);
        hu.setLayoutX(204.0);
        hu.setLayoutY(80.0);
        AnchorPane.setLeftAnchor(hu,204.0);
        AnchorPane.setRightAnchor(hu,116.0);

        en.setLayoutX(116.0);
        en.setLayoutY(80.0);
        AnchorPane.setLeftAnchor(en,116.0);
        AnchorPane.setRightAnchor(en,204.0);

        submit.setOnAction(event -> {
            if (en.isSelected()) {
                try {
                    Stage stage = new Stage();
                    Scene scene = null;
                    Parent root = null;
                    System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources\\sample-en.fxml");
                    root = FXMLLoader.load(getClass().getResource("/sample-en.fxml"));

                    stage.setTitle("Youtube video downloader");
                    stage.getIcons().add(new Image("/youtube.jpeg"));
                    stage.setResizable(false);
                    assert root != null;
                    scene = new Scene(root, 600, 650);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pane.getChildren().addAll(submit,en,hu);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
