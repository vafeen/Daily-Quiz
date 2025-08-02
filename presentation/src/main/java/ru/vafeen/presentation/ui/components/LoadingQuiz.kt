package ru.vafeen.presentation.ui.components

import RadialGradientLoader
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Компонент загрузочного экрана викторины.
 *
 * Отображает центрированный индикатор загрузки с отступом сверху.
 */
@Composable
internal fun LoadingQuiz() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(121.dp))
        RadialGradientLoader()
    }
}
