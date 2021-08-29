package com.neutron.controllers

import com.neutron.ColorSerializer
import com.neutron.Main
import com.neutron.SerializerData
import com.neutron.Writer
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Reset {
    @FXML
    var root = AnchorPane()
    fun no() {
        Main.s.s.close()
    }
    fun yes() {
        Main.s.ser = SerializerData(ColorSerializer(
            "ffffffff",
            "ffffffff",
            "ffffffff"), Main.s.ser.language)
        Writer.bufferedWriter(text = Json.encodeToString(Main.s.ser), "/data.json")
        Main.s.c.loadSettings()
        Main.s.s.close()
    }
}