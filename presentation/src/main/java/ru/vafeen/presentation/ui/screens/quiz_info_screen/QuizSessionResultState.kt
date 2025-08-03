package ru.vafeen.presentation.ui.screens.quiz_info_screen

import ru.vafeen.domain.models.QuizResult
import ru.vafeen.domain.models.QuizSessionResult

/**
 * Состояние экрана с результатами сессии викторины.
 *
 * @property quizResult рассчитанный результат викторины, основанный на количестве правильных ответов
 * @property quizSessionResult данные сессии викторины, включая подробности и статистику
 */
data class QuizSessionResultState(
    val quizResult: QuizResult? = null,
    val quizSessionResult: QuizSessionResult? = null,
)
