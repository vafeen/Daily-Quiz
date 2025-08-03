package ru.vafeen.presentation.utils


import android.content.Context

/**
 * Переводит значение из пикселей (px) в density-independent pixels (dp).
 *
 * @param this Значение в пикселях.
 * @param context Контекст для доступа к ресурсам и плотности экрана.
 * @return Значение в dp.
 */
fun Int.pxToDp(context: Context): Float = this / context.resources.displayMetrics.density
