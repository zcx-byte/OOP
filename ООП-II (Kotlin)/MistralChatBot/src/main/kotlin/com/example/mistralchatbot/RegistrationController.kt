package com.example.mistralchatbot

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegistrationController {

    // @FXML связывает переменную с элементом в FXML файле
    @FXML
    private lateinit var nameField: TextField

    @FXML
    private lateinit var startButton: Button

    // Эта функция вызывается при нажатии на кнопку
    @FXML
    private fun handleStartButton() {
        val userName = nameField.text.trim()

        // Проверяем, что имя не пустое
        if (userName.isEmpty()) {
            nameField.promptText = "Введите имя!"
            return
        }

        // Сохраняем имя пользователя в файл
        saveUser(userName)

        // Открываем главное окно чата
        openChatWindow(userName)
    }

    // Функция сохранения пользователя
    private fun saveUser(name: String) {

        // Создаём или открываем файл users.txt
        // true означает, что мы дописываем в конец файла
        FileWriter("users.txt", true).use { writer ->

            // Получаем текущую дату и время
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

            // Записываем: дата - имя
            writer.write("$dateTime - $name\n")
        }
    }

    // Функция открытия окна чата
    private fun openChatWindow(userName: String) {
        try {

            // Загружаем FXML файл главного окна
            val loader = FXMLLoader(javaClass.getResource("hello-view.fxml"))
            val root = loader.load<VBox>()

            // Получаем контроллер главного окна и передаём ему имя
            val chatController = loader.getController<ChatController>()
            chatController.setUserName(userName)

            // Создаём новую сцену
            val scene = Scene(root, 800.0, 600.0)

            // Получаем текущее окно (Stage)
            val stage = nameField.scene.window as Stage

            // Настраиваем окно
            stage.title = "Чат с Mistral AI - $userName"
            stage.scene = scene
            stage.show()

            // Закрываем окно регистрации
            (nameField.scene.window as Stage).close()

        } catch (e: Exception) {
            println("Ошибка при открытии окна: ${e.message}")
            e.printStackTrace()
        }
    }
}