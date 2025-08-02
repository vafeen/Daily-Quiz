package ru.vafeen.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.vafeen.domain.SomeInterface
import ru.vafeen.presentation.ui.theme.DailyQuizTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var someInterface: SomeInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test", "${someInterface.someValue}")
        enableEdgeToEdge()
        setContent {
            DailyQuizTheme {

            }
        }
    }
}