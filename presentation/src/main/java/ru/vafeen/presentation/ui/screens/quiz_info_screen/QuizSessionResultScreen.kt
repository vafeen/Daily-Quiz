package ru.vafeen.presentation.ui.screens.quiz_info_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.navigation.NavRootIntent

@Composable
internal fun QuizSessionResultScreen(
    sessionId: Long,
    sendRootIntent: (NavRootIntent) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("quizInfoScreen $sessionId")
        }
    }
}