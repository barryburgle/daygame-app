package com.barryburgle.gameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.barryburgle.gameapp.database.session.GameAppDatabase
import com.barryburgle.gameapp.ui.input.InputScreen
import com.barryburgle.gameapp.ui.input.InputViewModel
import com.barryburgle.gameapp.ui.theme.GameAppOriginalTheme


class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            GameAppDatabase::class.java,
            "game_app_db"
        ).build()
    }
    private val viewModel by viewModels<InputViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InputViewModel(db.abstractSessionDao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameAppOriginalTheme {
                val state by viewModel.state.collectAsState()
                InputScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
    }
}