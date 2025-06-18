package io.github.warforged5.theorygames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.warforged5.theorygames.ui.theme.TheoryGamesTheme
import io.github.warforged5.theorygames.dataclass.TheoryGamesApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TheoryGamesTheme {
                TheoryGamesApp()
            }
        }
    }
}