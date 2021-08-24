package com.neutron;

import javafx.stage.Stage;

public class Shared {
    public Controller c;
    public Stage s;
    public Shared() {}
    public Shared(Controller c) {
        this.c = c;
    }
    public Shared(Controller c, Stage s) {
        this.c = c;
        this.s = s;
    }
}
