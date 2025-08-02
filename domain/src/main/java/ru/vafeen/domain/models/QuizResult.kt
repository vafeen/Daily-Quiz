package ru.vafeen.domain.models

/**
 * Перечисление итоговых результатов викторины в зависимости от количества правильных ответов.
 *
 * @property correctAnswers Количество правильных ответов (0..5).
 */
enum class QuizResult(val correctAnswers: Int) {
    Score0Of5(0),
    Score1Of5(1),
    Score2Of5(2),
    Score3Of5(3),
    Score4Of5(4),
    Score5Of5(5);

    companion object {
        /**
         * Возвращает элемент [QuizResult], соответствующий количеству правильных ответов [count].
         * Если не найдено подходящего значения, возвращает [Score0Of5].
         *
         * @param count Количество правильных ответов.
         * @return Соответствующий результат квиза.
         */
        fun getByCount(count: Int): QuizResult =
            entries.firstOrNull { it.correctAnswers == count } ?: Score0Of5
    }
}