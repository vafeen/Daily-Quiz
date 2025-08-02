package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vafeen.data.local_database.converter.RoomTypeConverters
import ru.vafeen.data.local_database.dao.QuizDao
import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity

/**
 * Основная база данных Room для хранения данных викторины.
 *
 * Хранит сессии викторины и результаты вопросов.
 *
 * @see QuizSessionEntity
 * @see QuizQuestionResultEntity
 * @see QuizDao
 */
@Database(
    entities = [
        QuizSessionEntity::class,
        QuizQuestionResultEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
internal abstract class AppDatabase : RoomDatabase() {

    /**
     * Получить DAO для работы с данными викторины.
     *
     * @return Экземпляр [QuizDao].
     */
    abstract fun quizDao(): QuizDao

    companion object {
        /**
         * Имя файла базы данных.
         */
        const val NAME = "DailyQuizDb"
    }
}
