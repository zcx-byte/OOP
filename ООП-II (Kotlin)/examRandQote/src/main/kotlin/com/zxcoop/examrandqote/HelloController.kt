package com.zxcoop.examrandqote

import javafx.fxml.FXML
import javafx.scene.control.TextArea


class HelloController {

    @FXML
    private lateinit var textOutput: TextArea

    private val generatorL = GeneratorPhraseL()

    private val generatorF = GeneratorPhraseF()

    @FXML
    private fun onSendButton() {

        val phrase = generatorL.generatrePhrase()
        textOutput.text = phrase
    }

    @FXML
    private fun onAtFile(){

        val phrase = generatorF.generatrePhrase()
        textOutput.text = phrase
    }
}