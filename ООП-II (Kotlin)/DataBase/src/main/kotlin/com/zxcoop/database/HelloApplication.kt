package com.zxcoop.database

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("main_window.fxml"))
        val scene = Scene(fxmlLoader.load(), 555.0, 440.0)
        stage.title = "Добро пожаловать в БД!!"
        stage.scene = scene
        stage.show()
    }
}

