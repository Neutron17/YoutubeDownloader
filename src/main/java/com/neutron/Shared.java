package com.neutron;

import com.neutron.controllers.Controller;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Shared {
    public Controller c;
    public Stage s;
    public SerializerData ser;
    public static TableView<Element> table;
    public Shared() {
        this.ser = Controller.decode();}
    public Shared(Controller c) {
        this.c = c;
    }
    public Shared(Controller c, Stage s) {
        this.c = c;
        this.s = s;
        this.ser = Controller.decode();
    }
    public Shared(Controller c, Stage s, SerializerData ser) {
        this.c = c;
        this.s = s;
        this.ser = ser;
    }
}
