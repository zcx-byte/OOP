package com.example.mistralchatbot

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {

    override fun start(stage: Stage) {
        // Загружаем окно регистрации
        val loader = FXMLLoader(javaClass.getResource("registration-view.fxml"))

        try {
            val scene = Scene(loader.load(), 400.0, 300.0)
            stage.title = "Регистрация - Mistral Chat Bot"
            stage.scene = scene
            stage.show()
        } catch (e: Exception) {
            println("Ошибка при загрузке: ${e.message}")
            e.printStackTrace()
        }
    }
}

// Точка входа в приложение
fun main() {
    Application.launch(HelloApplication::class.java)
}