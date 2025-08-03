package ru.vafeen.presentation.utils

import android.content.Context

/**
 * Переводит значение из пикселей (px) в density-independent pixels (dp).
 *
 * @receiver Значение в пикселях.
 * @param context Контекст для доступа к ресурсам и плотности экрана.
 * @return Значение в dp.
 */
fun Int.pxToDp(context: Context): Float = this / context.resources.displayMetrics.density

/**
 * Форматирует целочисленное значение секунд в строку формата "минуты:секунды" с ведущим нулём у секунд.
 *
 * Пример:
 * 90.secondsToMinSecString() -> "01:30"
 * 5.secondsToMinSecString() -> "00:05"
 *
 * @receiver Количество секунд в формате Int.
 * @return Строка с временем в формате "минуты:секунды".
 */
internal fun Int.secondsToMinSecString(): String = "%02d:%02d".format(this / 60, this % 60)

