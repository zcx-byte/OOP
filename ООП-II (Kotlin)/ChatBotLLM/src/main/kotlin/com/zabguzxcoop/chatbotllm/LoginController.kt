package com.zabguzxcoop.chatbotllm

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.stage.Stage

/**
 * Контроллер окна авторизации.
 * Принимает имя пользователя и открывает чат.
 */
class LoginController {
    @FXML private lateinit var usernameField: TextField
    private lateinit var primaryStage: Stage

    fun setPrimaryStage(stage: Stage) {
        primaryStage = stage
    }

    /**
     * Обработчик кнопки "Войти".
     */
    @FXML
    private fun handleLogin() {
        val username = usernameField.text.trim()
        if (username.isEmpty()) return // Простая валидация

        try {

            // Загружаем FXML чата
            val loader = FXMLLoader(javaClass.getResource("/com/zabguzxcoop/chatbotllm/chat-view.fxml"))
            val root: Parent = loader.load()

            // Передаём данные в контроллер чата
            val chatController = loader.getController<ChatController>()
            chatController.setUsername(username)

            chatController.setPrimaryStage(primaryStage)

            // Обновляем сцену и заголовок окна
            primaryStage.apply {

                title = "ChatBot — $username"

                scene = Scene(root, 800.0, 600.0)
                show()
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}