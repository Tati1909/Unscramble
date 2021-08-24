package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
//!!Изменяемые данные внутри ViewModel всегда должны быть private.

    //Переменные определены для текущего зашифрованного слова ( currentScrambledWord),
    // количества слов ( currentWordCount) и
    // оценки ( score).
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    //_currentScrambledWord доступно и редактируется только в GameViewModel.
    // Контроллер пользовательского интерфейса GameFragment может только считывать значение
    // с помощью public свойства currentScrambledWord,
    // доступного только для чтения
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
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
            //Чтобы получить доступ к данным внутри LiveData объекта, используйте value свойство.
            _currentScrambledWord.value = String(tempWord)
                //++_currentWordCount.value - так опасно делать, используем юезопасную инкрементацию
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    /*
* Возвращает true, если текущее количество слов меньше MAX_NO_OF_WORDS.
* Обновляет следующее слово.
*/
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    // метод, вызываемый без параметров и без возвращаемого значения.
    // Увеличьте score переменную на SCORE_INCREASE.
    //Используйте plus()функцию Kotlin, которая выполняет добавление с нулевой безопасностью.
    //было так: _score.value += SCORE_INCREASE
    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    // Сверяем слово игрока  и увеличиваем счет , если догадка верна.
    // Это обновит окончательный счет в нашем диалоговом окне предупреждения.
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /*
* Повторно инициализирует данные игры, чтобы перезапустить игру.
* */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel уничтожилась!")
    }
}