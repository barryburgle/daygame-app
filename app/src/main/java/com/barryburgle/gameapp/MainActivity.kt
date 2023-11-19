package com.barryburgle.gameapp

import android.content.pm.ActivityInfo
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
import com.barryburgle.gameapp.ui.navigation.Navigation
import com.barryburgle.gameapp.ui.input.InputViewModel
import com.barryburgle.gameapp.ui.output.OutputViewModel
import com.barryburgle.gameapp.ui.theme.GameAppOriginalTheme
import com.barryburgle.gameapp.ui.tool.ToolViewModel


class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            GameAppDatabase::class.java,
            "game_app_db"
        ).build()
    }
    private val inputViewModel by viewModels<InputViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InputViewModel(db.abstractSessionDao) as T
                }
            }
        }
    )
    private val outputViewModel by viewModels<OutputViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OutputViewModel(db.abstractSessionDao, db.aggregatedStatDao) as T
                }
            }
        }
    )
    private val toolViewModel by viewModels<ToolViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ToolViewModel(db.abstractSessionDao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            GameAppOriginalTheme {
                val inputState by inputViewModel.state.collectAsState()
                val outputState by outputViewModel.state.collectAsState()
                val toolsState by toolViewModel.state.collectAsState()
                Navigation(
                    inputState = inputState,
                    outputState = outputState,
                    toolState = toolsState,
                    inputOnEvent = inputViewModel::onEvent,
                    outputOnEvent = outputViewModel::onEvent,
                    toolOnEvent = toolViewModel::onEvent
                )
            }
        }
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
    }
}