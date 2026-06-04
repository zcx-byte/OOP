package com.zxcoop.examrandqote

import com.zxcoop.examrandqote.interf.GeneratePhrasee
import com.zxcoop.examrandqote.interf.IGenetatorPhraseF
import java.io.File

class GeneratorPhraseF: GeneratePhrasee {

    private var filename = "phrases.txt"

    override fun generatrePhrase(): String {

        val file = File(filename)

        val line = file.readLines()

        return line.random()

    }
}