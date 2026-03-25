package com.zabguzxcoop.chatbotllm

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        try {
            val loader = FXMLLoader(javaClass.getResource("/com/zabguzxcoop/chatbotllm/login-view.fxml"))
            val root: Parent = loader.load()

            @Suppress("UNCHECKED_CAST")
            val controller = loader.getController() as LoginController
            controller.setPrimaryStage(stage)

            val scene = Scene(root, 500.0, 400.0)

            stage.title = "ChatBot LLM"
            stage.scene = scene
            stage.isResizable = true
            stage.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}