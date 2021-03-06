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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
// Раздутие XML-файла макета и возврат экземпляра объекта привязки
// Было binding = GameFragmentBinding.inflate(inflater, container, false)
        // Подключили databinding в build.gradle
        //buildFeatures {
        //   dataBinding = true
        //}
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d(
            "GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                    "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}"
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // инициализируйте переменные макета gameViewModel и maxNoOfWords с помощью dataBinding
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Укажите вью фрагмента как владельца жизненного цикла привязки.
        // Это используется для того, чтобы привязка dataBinding могла видеть обновления LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
    }

    /*
    * обработка кнопки submit(отправить)
    * Если true доступно другое слово, обновите зашифрованное слово на экране.
    *  В противном случае игра окончена, поэтому отобразите диалоговое окно предупреждения с окончательным счетом.
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        //проверяем слово игрока
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true) //Если слово игрока неверно, показываем сообщение об ошибке в текстовом поле
        }
    }

    /*
     * onSkipWord()является прослушивателем щелчков для кнопки « Пропустить» ,
     * эта функция обновляет пользовательский интерфейс аналогично onSubmitWord(),
     * за исключением оценки.
     */
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            //updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    /*
     * restartGame()и exitGame()функции используются для перезапуска и завершения игры соответственно
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        //updateNextWordOnScreen()
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
* Создает и показывает AlertDialog с окончательной оценкой.*/
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false) //Сделали диалоговое окно предупреждения не отменяемым
            // при нажатии клавиши возврата, используя setCancelable()метод и передачу false.
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }
}
