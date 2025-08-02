package ru.vafeen.data.network.converter

import ru.vafeen.data.network.dto.QuizQuestionDto
import ru.vafeen.domain.models.QuizQuestion


/**
 * Преобразует DTO вопроса викторины в доменную модель.
 *
 * @receiver DTO вопроса викторины.
 * @return Доменная модель вопроса викторины с перемешанными ответами.
 */
internal fun QuizQuestionDto.toDomainModel(): QuizQuestion =
    QuizQuestion(
        question = question,
        category = category,
        difficulty = difficulty,
        correctAnswer = correctAnswer,
        allAnswers = (incorrectAnswers + correctAnswer).shuffled()
    )
