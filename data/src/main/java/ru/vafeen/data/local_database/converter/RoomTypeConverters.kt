package ru.vafeen.data.local_database.converter

import androidx.room.TypeConverter
import ru.vafeen.domain.models.QuizResult
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Конвертеры для нестандартных типов, используемых в Room-сущностях.
 */
internal object RoomTypeConverters {

    private const val SEPARATOR = ";;;"

    /**
     * Конвертирует [LocalDateTime] в [Long] (миллисекунды с эпохи).
     *
     * @param dateTime Входящий объект LocalDateTime.
     * @return Время в миллисекундах с эпохи.
     */
    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(dateTime: LocalDateTime): Long =
        dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    /**
     * Конвертирует [Long] (миллисекунды с эпохи) обратно в [LocalDateTime].
     *
     * @param millis Время в миллисекундах с эпохи.
     * @return Объект LocalDateTime.
     */
    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(millis: Long): LocalDateTime =
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()

    /**
     * Конвертирует список строк в одну строку с разделителем.
     *
     * @param list Список строк.
     * @return Однострочное представление.
     */
    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>): String = list.joinToString(SEPARATOR)

    /**
     * Конвертирует строку с разделёнными элементами обратно в список строк.
     *
     * @param data Строка с разделёнными элементами.
     * @return Список строк.
     */
    @TypeConverter
    @JvmStatic
    fun toStringList(data: String): List<String> =
        if (data.isEmpty()) emptyList() else data.split(SEPARATOR)

    /**
     * Конвертирует [QuizResult] в [Int], для хранения количества правильных ответов.
     *
     * @param result Результат викторины.
     * @return Количество правильных ответов.
     */
    @TypeConverter
    @JvmStatic
    fun fromQuizResult(result: QuizResult): Int = result.correctAnswers

    /**
     * Восстанавливает [QuizResult] из количества правильных ответов.
     *
     * @param count Количество правильных ответов.
     * @return Экземпляр QuizResult.
     */
    @TypeConverter
    @JvmStatic
    fun toQuizResult(count: Int): QuizResult = QuizResult.getByCount(count)
}
