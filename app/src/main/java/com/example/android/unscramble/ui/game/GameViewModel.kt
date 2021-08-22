package com.example.android.unscramble.ui.game

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel()  {
//!!Изменяемые данные внутри ViewModel всегда должны быть private.

    //Переменные определены для текущего зашифрованного слова ( currentScrambledWord),
    // количества слов ( currentWordCount) и оценки ( score).
    private var score = 0
    private var currentWordCount = 0
    //_currentScrambledWord доступно и редактируется только в GameViewModel.
    // Контроллер пользовательского интерфейса GameFragment может только считывать значение
    // с помощью public свойства currentScrambledWord,
    // доступного только для чтения
    private var _currentScrambledWord = "test"
    val currentScrambledWord: String
        get() = _currentScrambledWord
}