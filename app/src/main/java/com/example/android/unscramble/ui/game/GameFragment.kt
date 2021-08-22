/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding

/**
 * Логика игры, связь между View и ViewModel
 * GameFragment как Контроллер
 */
class GameFragment : androidx.fragment.app.Fragment() {

    //добавили ссылку(объект) на GameViewModel
    //Делегирование свойств в Kotlin помогает передать ответственность за геттер-сеттер другому классу.
    //Этот класс (называемый классом делегата ) предоставляет функции получения и установки свойства и обрабатывает его изменения.
    //Свойство делегата определяется с помощью by и экземпляра класса делегата:
    //var <property-name> : <property-type> by <delegate-class>()
    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
                R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    /*
    * onSubmitWord()является прослушивателем кликов для кнопки « Отправить» ,
    *  эта функция отображает следующее зашифрованное слово, очищает текстовое поле и
    * увеличивает счет и количество слов без проверки слова игрока.
    */
    private fun onSubmitWord() {

    }

    /*
     * onSkipWord()является прослушивателем щелчков для кнопки « Пропустить» ,
     * эта функция обновляет пользовательский интерфейс аналогично onSubmitWord(),
     * за исключением оценки.
     */
    private fun onSkipWord() {

    }

    /*
     * getNextScrambledWord() - это вспомогательная функция,
     * которая выбирает случайное слово из списка слов и перемешивает буквы в нем
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    /*
     * restartGame()и exitGame()функции используются для перезапуска и завершения игры соответственно,
     *  вы будете использовать эти функции позже.
     */
    private fun restartGame() {
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
Устанавливает и сбрасывает статус ошибки текстового поля.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
     * updateNextWordOnScreen() функция отображает новое зашифрованное слово.
     */
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }
}
