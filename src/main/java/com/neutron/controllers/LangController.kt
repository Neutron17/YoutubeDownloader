package com.neutron.controllers

import com.neutron.Main
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ToggleButton
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.IOException

class LangController {
    @FXML
    lateinit var en: ToggleButton
    @FXML
    lateinit var hu: ToggleButton

    @FXML
    fun submitHandler() {
        if (en.isSelected) {
            try {
                val root = FXMLLoader.load<Parent>(javaClass.getResource("/sample-en.fxml"))
                Main.stage = Stage()
                assert(root != null)
                Main.scene = Scene(root, 600.0, 650.0)
                Main.stage.title = "Youtube video downloader"
                Main.stage.icons.add(Image("/youtube2.png"))
                Main.stage.isResizable = false
                Main.stage.scene = Main.scene
                Main.stage.show()
            } catch (e: IOException) {
                e.printStackTrace()
                Main.error(e)
            }
        } else if (hu.isSelected) {
            try {
                val root = FXMLLoader.load<Parent>(javaClass.getResource("/sample-hu.fxml"))
                Main.stage = Stage()
                assert(root != null)
                Main.scene = Scene(root, 600.0, 650.0)
                Main.stage.setTitle("Youtube videó letöltő")
                Main.stage.getIcons().add(Image("/youtube.jpeg"))
                Main.stage.setResizable(false)
                Main.stage.setScene(Main.scene)
                Main.stage.show()
            } catch (e: IOException) {
                e.printStackTrace()
                Main.error(e)
            }
        }
        primaryStage.close()
    }
    companion object {
        lateinit var primaryStage: Stage
    }
}