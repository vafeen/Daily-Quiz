package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.presentation.R

/**
 * Компонент отображения сообщения об ошибке на экране викторины.
 *
 * Отображает текст с сообщением об ошибке с отступом сверху,
 * белым цветом и увеличенным размером шрифта для заметности.
 */
@Composable
internal fun Error() {
    Text(
        modifier = Modifier.padding(top = 24.dp),
        text = stringResource(R.string.error_try_again),
        fontSize = 20.sp,
        color = Color.White
    )
}
