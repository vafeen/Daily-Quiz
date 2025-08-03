package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.presentation.R

/**
 * Компонент приветственного экрана с кнопкой начала викторины.
 *
 * Отображает карточку с заголовком и кнопкой "Начать квиз".
 *
 * @param onBegin Лямбда, вызываемая при нажатии кнопки запуска викторины.
 */
@Composable
internal fun Welcome(onBegin: () -> Unit) {
    Column(modifier = Modifier.padding(top = 40.dp)) {
        Card(
            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_dailyquiz),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))
                RounderCornerButton(
                    onClick = onBegin,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = stringResource(R.string.start_the_quiz).uppercase(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
