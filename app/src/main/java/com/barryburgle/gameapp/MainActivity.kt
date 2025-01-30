package com.barryburgle.gameapp

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.database.session.GameAppDatabase
import com.barryburgle.gameapp.service.notification.NotificationService
import com.barryburgle.gameapp.ui.input.InputViewModel
import com.barryburgle.gameapp.ui.navigation.Navigation
import com.barryburgle.gameapp.ui.output.OutputViewModel
import com.barryburgle.gameapp.ui.stats.StatsViewModel
import com.barryburgle.gameapp.ui.theme.GameAppOriginalTheme
import com.barryburgle.gameapp.ui.tool.ToolViewModel


class MainActivity : ComponentActivity() {

    private val db by lazy {
        GameAppDatabase.getInstance(applicationContext)
    }

    private val inputViewModel by viewModels<InputViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return db?.let {
                        InputViewModel(
                            applicationContext,
                            it.abstractSessionDao,
                            it.settingDao,
                            it.leadDao
                        )
                    } as T
                }
            }
        }
    )

    private val outputViewModel by viewModels<OutputViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return db?.let {
                        OutputViewModel(
                            it.abstractSessionDao,
                            it.aggregatedStatDao,
                            it.settingDao,
                            it.leadDao
                        )
                    } as T
                }
            }
        }
    )

    private val statsViewModel by viewModels<StatsViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return db?.let { StatsViewModel(it.abstractSessionDao, it.leadDao) } as T
                }
            }
        }
    )

    private val toolViewModel by viewModels<ToolViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return db?.let { ToolViewModel(it.abstractSessionDao, it.settingDao) } as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (!checkPermission(applicationContext)) {
            requestPermission(applicationContext)
        }
        setContent {
            GameAppOriginalTheme {
                val inputState by inputViewModel.state.collectAsState()
                val outputState by outputViewModel.state.collectAsState()
                val statsState by statsViewModel.state.collectAsState()
                val toolsState by toolViewModel.state.collectAsState()
                Navigation(
                    inputState = inputState,
                    outputState = outputState,
                    statsState = statsState,
                    toolState = toolsState,
                    inputOnEvent = inputViewModel::onEvent,
                    toolOnEvent = toolViewModel::onEvent
                )
            }
        }
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationService.STICKING_POINT_NOTIFICATION_CHANNEL_ID,
                NotificationService.STICKING_POINT_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Daygame sticking point notification"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readFile = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            val writeFile = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            readFile && writeFile
        }
    }

    private fun requestPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(String.format("package:%s", context.packageName))
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        } else {
            val requestReadWritePermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                var count: kotlin.Int = 0
                result.entries.forEach {
                    if (it.value == true) {
                        count++
                    }
                    if (count == 2) {
                        Toast.makeText(
                            applicationContext,
                            "File access permission granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "File access permission not granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            requestReadWritePermissionLauncher.launch(
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }
}