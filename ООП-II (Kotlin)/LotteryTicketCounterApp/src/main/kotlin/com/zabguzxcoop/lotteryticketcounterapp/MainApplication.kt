package com.zabguzxcoop.lotteryticketcounterapp

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main_window.fxml"))
        val scene = Scene(fxmlLoader.load(), 514.0, 640.0)
        stage.title = "Касса Лотерейных билетов"
        stage.scene = scene
        stage.show()
    }
}
  
