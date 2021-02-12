package neutron;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        en.setText("EN");
        ToggleButton hu = new ToggleButton();
        hu.setText("HU");
        ToggleGroup lang = new ToggleGroup();
        en.setToggleGroup(lang);
        hu.setToggleGroup(lang);
        hu.setLayoutX(204.0);
        hu.setLayoutY(80.0);
        AnchorPane.setLeftAnchor(hu,200.0);
        AnchorPane.setRightAnchor(hu,100.0);

        en.setLayoutX(116.0);
        en.setLayoutY(80.0);
        AnchorPane.setLeftAnchor(en,100.0);
        AnchorPane.setRightAnchor(en,200.0);

        submit.setOnAction(event -> {
            if (en.isSelected()) {
                Main.en(primaryStage);
                primaryStage.close();
            }else if(hu.isSelected()) {
                Main.hu(primaryStage);
                primaryStage.close();
            }else {
                Stage stage = new Stage();
                stage.setScene(new Scene(new Pane(new Label("Please select one!"))));
                stage.show();
            }
        });
        pane.getChildren().addAll(submit,en,hu);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
