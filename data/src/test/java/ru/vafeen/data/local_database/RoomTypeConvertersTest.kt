package ru.vafeen.data.local_database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.vafeen.data.local_database.converter.RoomTypeConverters
import ru.vafeen.domain.models.QuizResult
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class RoomTypeConvertersTest {

    /**
     * Проверяет конвертацию [LocalDateTime] в [Long] и обратно,
     * убеждаясь в сохранении времени до секунды.
     */
    @Test
    fun `fromLocalDateTime and toLocalDateTime convert correctly and preserve time`() {
        val dateTime = LocalDateTime.of(2024, 8, 3, 15, 45, 30)
        val millis = RoomTypeConverters.fromLocalDateTime(dateTime)
        val convertedBack = RoomTypeConverters.toLocalDateTime(millis)
        assertEquals(
            dateTime.truncatedTo(ChronoUnit.SECONDS),
            convertedBack.truncatedTo(ChronoUnit.SECONDS)
        )
    }

    /**
     * Проверяет использование эпохальнго времени из 0 в конвертации обратно в LocalDateTime.
     */
    @Test
    fun `toLocalDateTime converts zero millis to epoch start LocalDateTime`() {
        val epochStart = RoomTypeConverters.toLocalDateTime(0)
        assertEquals(1970, epochStart.year)
        assertEquals(1, epochStart.monthValue)
        assertEquals(1, epochStart.dayOfMonth)
        assertEquals(3, epochStart.hour)
        assertEquals(0, epochStart.minute)
    }

    /**
     * Проверяет конвертацию пустого списка строк в пустую строку.
     */
    @Test
    fun `fromStringList converts empty list to empty string`() {
        val list = emptyList<String>()
        val result = RoomTypeConverters.fromStringList(list)
        assertEquals("", result)
    }

    /**
     * Проверяет конвертацию списка со специальными символами и пустыми строками в строку через разделитель.
     */
    @Test
    fun `fromStringList converts list with special characters correctly`() {
        val list = listOf("", "hello", "world;;;escape", "test")
        val result = RoomTypeConverters.fromStringList(list)
        assertEquals(";;;hello;;;world;;;escape;;;test", result)
    }

    /**
     * Проверяет конвертацию списка из одного элемента в правильную строку без разделителей.
     */
    @Test
    fun `fromStringList converts single element list correctly`() {
        val list = listOf("singleElement")
        val result = RoomTypeConverters.fromStringList(list)
        assertEquals("singleElement", result)
    }

    /**
     * Проверяет преобразование пустой строки в пустой список.
     */
    @Test
    fun `toStringList converts empty string to empty list`() {
        val input = ""
        val list = RoomTypeConverters.toStringList(input)
        assertTrue(list.isEmpty())
    }

    /**
     * Проверяет конвертацию строки с разделителем в список строк.
     */
    @Test
    fun `toStringList converts delimited string to list correctly`() {
        val input = "one;;;two;;;three"
        val list = RoomTypeConverters.toStringList(input)
        assertEquals(listOf("one", "two", "three"), list)
    }

    /**
     * Проверяет конвертацию строки без разделителей в список из одного элемента.
     */
    @Test
    fun `toStringList converts string without separators to single element list`() {
        val input = "nosplitstring"
        val list = RoomTypeConverters.toStringList(input)
        assertEquals(listOf("nosplitstring"), list)
    }

    /**
     * Проверяет обратимость конвертации списка строк — из списка в строку и обратно.
     */
    @Test
    fun `stringList conversion is reversible`() {
        val originalList = listOf("A", "B", "C", "D", "", "E")
        val serialized = RoomTypeConverters.fromStringList(originalList)
        val deserialized = RoomTypeConverters.toStringList(serialized)
        assertEquals(originalList, deserialized)
    }

    /**
     * Проверяет конвертацию [QuizResult] в Int — количество правильных ответов.
     */
    @Test
    fun `fromQuizResult returns correct number of correct answers`() {
        assertEquals(0, RoomTypeConverters.fromQuizResult(QuizResult.Score0Of5))
        assertEquals(3, RoomTypeConverters.fromQuizResult(QuizResult.Score3Of5))
        assertEquals(5, RoomTypeConverters.fromQuizResult(QuizResult.Score5Of5))
    }

    /**
     * Проверяет конвертацию Int в правильный [QuizResult].
     */
    @Test
    fun `toQuizResult returns correct QuizResult from valid count`() {
        (0..5).forEach { count ->
            val result = RoomTypeConverters.toQuizResult(count)
            assertEquals(count, result.correctAnswers)
        }
    }

    /**
     * Проверяет, что для неподдерживаемого количества count возвращается ближайший валидный [QuizResult].
     */
    @Test
    fun `toQuizResult clamps invalid counts to valid QuizResult`() {
        val negativeResult = RoomTypeConverters.toQuizResult(-1)
        assertEquals(0, negativeResult.correctAnswers)

        val largeResult = RoomTypeConverters.toQuizResult(10)
        assertTrue(largeResult.correctAnswers in 0..5)
    }
}
