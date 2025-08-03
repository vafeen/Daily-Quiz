package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme

@Composable
internal fun TimeIsUpDialog(onTryAgainClick: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.cardBackground)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.time_is_up),
                    color = AppTheme.colors.text,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.didnt_have_time),
                    color = AppTheme.colors.text,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))
                RounderCornerButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    onClick = onTryAgainClick,
                    containerColor = AppTheme.colors.background
                ) {
                    Text(stringResource(R.string.try_again).uppercase())
                }
            }
        }
    }
}