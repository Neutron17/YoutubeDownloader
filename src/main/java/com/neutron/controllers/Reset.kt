package com.neutron.controllers

import com.neutron.Main
import com.neutron.Writer
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane

class Reset {
    @FXML
    var root = AnchorPane()
    fun no() {
        Main.s.s.close()
    }
    fun yes() {
        Writer.bufferedWriter("0xffffffff", "/mainBg.txt")
        Writer.bufferedWriter("0xffffffff", "/historyBg.txt")
        Writer.bufferedWriter("0xffffffff", "/settingsBg.txt")
        Main.s.c.loadSettings()
        Main.s.s.close()
    }
}