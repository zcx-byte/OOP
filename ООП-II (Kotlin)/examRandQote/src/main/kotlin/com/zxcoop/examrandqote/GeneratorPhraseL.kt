package com.zxcoop.examrandqote

import com.zxcoop.examrandqote.interf.GeneratePhrasee
import com.zxcoop.examrandqote.interf.IGeneratorPhraseL


class GeneratorPhraseL: GeneratePhrasee {

    private val qotes = listOf(
        "На смену пейзажу с окутанными снегом деревьями пришла нежная акварель весны.",
        "Семья — это вовсе не список, кто кого родил",
        "Kotlin - хороший язык для программирвоания",
        "Стремитесь не к успеху, а к ценностям, которые он дает",
        "Настоящая ответственность бывает только личной."
    )
//
//    override fun generatePhrase(): String = qotes.random()

    override fun generatrePhrase() = qotes.random()
}