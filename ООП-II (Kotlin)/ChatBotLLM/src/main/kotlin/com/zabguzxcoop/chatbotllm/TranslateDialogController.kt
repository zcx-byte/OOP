package com.zabguzxcoop.chatbotllm

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextField
import java.io.File

/**
 * Контроллер для диалогового окна настройки перевода.
 * Хранит выбранный файл и языковые параметры.
 */
class TranslateDialogController {
    @FXML private lateinit var fileNameLabel: Label
    @FXML private lateinit var sourceLangField: TextField
    @FXML private lateinit var targetLangField: TextField

    private var selectedFile: File? = null

    /**
     * Устанавливает файл и обновляет подпись в интерфейсе.
     */
    fun setFile(file: File) {
        selectedFile = file
        fileNameLabel.text = "Файл: ${file.name}"
    }

    // Геттеры с значениями по умолчанию
    fun getSourceLanguage(): String = sourceLangField.text.trim().ifEmpty { "Russian" }
    fun getTargetLanguage(): String = targetLangField.text.trim().ifEmpty { "English" }
    fun getFile(): File? = selectedFile
}