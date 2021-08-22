package com.example.android.unscramble.ui.game

import android.util.Log
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
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel создался!")
        getNextWord()
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        //преобразуйте строку currentWord в массив символов
        val tempWord = currentWord.toCharArray()
        //перемешаем буквы
        tempWord.shuffle()
        //Иногда перетасованный порядок символов совпадает с исходным словом.
        // Добавьте следующий while цикл вокруг вызова перемешивания, чтобы продолжить цикл до тех пор,
        // пока зашифрованное слово не станет таким же, как исходное слово.
        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        //Добавьте if-else блок, чтобы проверить, использовалось ли уже слово. Если wordsList содержит currentWord,
        // вызовите getNextWord(). Если нет, обновите значение _currentScrambledWord с помощью нового
        // зашифрованного слова, увеличьте количество слов и добавьте новое слово в wordsList.
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++currentWordCount
            wordsList.add(currentWord)
        }
    }

    /*
* Возвращает true, если текущее количество слов меньше MAX_NO_OF_WORDS.
* Обновляет следующее слово.
*/
    fun nextWord(): Boolean {
        return if (currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel уничтожилась!")
    }
}