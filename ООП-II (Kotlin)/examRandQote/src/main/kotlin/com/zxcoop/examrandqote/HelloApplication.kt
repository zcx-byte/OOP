package com.zxcoop.examrandqote

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 410.0, 373.0)
        stage.title = "Генератор цитат"
        stage.scene = scene
        stage.show()
    }
}
  
