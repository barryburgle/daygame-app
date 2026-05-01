package com.barryburgle.gameapp

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.barryburgle.gameapp.api.RetrofitInstance
import com.barryburgle.gameapp.api.response.github.GithubLatestResponse
import com.barryburgle.gameapp.dao.setting.SettingDao
import com.barryburgle.gameapp.database.GameAppDatabase
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.service.notification.NotificationService
import com.barryburgle.gameapp.service.notification.PersistentNotificationService
import com.barryburgle.gameapp.ui.input.InputViewModel
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.navigation.Navigation
import com.barryburgle.gameapp.ui.output.OutputViewModel
import com.barryburgle.gameapp.ui.stats.StatsViewModel
import com.barryburgle.gameapp.ui.theme.GameAppOriginalTheme
import com.barryburgle.gameapp.ui.tool.ToolViewModel
import com.barryburgle.gameapp.ui.utilities.dialog.passInitialValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : ComponentActivity() {

    private val db by lazy {
        GameAppDatabase.getInstance(applicationContext)
    }

    private val inputViewModel by viewModels<InputViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return db?.let {
                    InputViewModel(
                        applicationContext,
                        it.abstractSessionDao,
                        it.settingDao,
                        it.leadDao,
                        it.dateDao,
                        it.setDao,
                        it.challengeDao,
                        it.aggregatedSessionsDao,
                        it.aggregatedDatesDao
                    )
                } as T
            }
        }
    })

    private val outputViewModel by viewModels<OutputViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return db?.let {
                    OutputViewModel(
                        it.abstractSessionDao,
                        it.aggregatedSessionsDao,
                        it.aggregatedDatesDao,
                        it.settingDao,
                        it.leadDao,
                        it.dateDao,
                        it.setDao
                    )
                } as T
            }
        }
    })

    private val statsViewModel by viewModels<StatsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return db?.let {
                    StatsViewModel(
                        it.abstractSessionDao,
                        it.leadDao,
                        it.dateDao,
                        it.challengeDao,
                        it.setDao,
                        it.settingDao
                    )
                } as T
            }
        }
    })

    private val toolViewModel by viewModels<ToolViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return db?.let {
                    ToolViewModel(
                        it.abstractSessionDao,
                        it.leadDao,
                        it.dateDao,
                        it.setDao,
                        it.challengeDao,
                        it.settingDao
                    )
                } as T
            }
        }
    })

    private fun triggerLiveSession(onEvent: (GameEvent) -> Unit, state: InputState) {
        val dateTime = passInitialValue(true, null, "")
        val context = this
        val intent =
            Intent(context, PersistentNotificationService::class.java).apply {
                putExtra(
                    PersistentNotificationService.LIVE_SESSIONS_START_HOUR,
                    dateTime.substring(11, 16)
                )
                putExtra(
                    PersistentNotificationService.IS_FOLLOW_COUNT_ACTIVE,
                    state.followCount
                )
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
        Toast.makeText(this, "Live session started", Toast.LENGTH_SHORT).show()
        moveTaskToBack(true)
    }

    private fun handleShortcut(intent: Intent?) {
        if (intent?.getStringExtra("shortcut_type") == "add_item") {
            triggerLiveSession(inputViewModel::onEvent, inputViewModel.state.value)
            intent.removeExtra("shortcut_type")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleShortcut(intent)
        createNotificationChannel()
        lifecycleScope.launch(Dispatchers.IO) {
            checkForUpdates()
        }
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        handlePermissionsFlow()
        setContent {
            val inputState by inputViewModel.state.collectAsState()
            val outputState by outputViewModel.state.collectAsState()
            val statsState by statsViewModel.state.collectAsState()
            val toolsState by toolViewModel.state.collectAsState()
            GameAppOriginalTheme(toolsState.theme, toolsState.themeSysFollow) {
                Navigation(
                    inputState = inputState,
                    outputState = outputState,
                    statsState = statsState,
                    toolState = toolsState,
                    inputOnEvent = inputViewModel::onEvent,
                    outputOnEvent = outputViewModel::onEvent,
                    statsOnEvent = statsViewModel::onEvent,
                    toolOnEvent = toolViewModel::onEvent
                )
            }
        }
        WindowCompat.setDecorFitsSystemWindows(
            window, false
        )
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        READ_CONTACTS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestContactPermissionLauncher.launch(READ_CONTACTS)
                }
            }
        }
    }

    private fun handlePermissionsFlow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android.intent.category.DEFAULT")
                    data = Uri.parse("package:$packageName")
                    flags = FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestContactPermissionLauncher.launch(READ_CONTACTS)
            }
        } else {
            val permissions = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(
                    this,
                    READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(READ_CONTACTS)
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(READ_EXTERNAL_STORAGE)
                permissions.add(WRITE_EXTERNAL_STORAGE)
            }
            if (permissions.isNotEmpty()) {
                requestLegacyPermissionsLauncher.launch(permissions.toTypedArray())
            }
        }
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notifications disabled", Toast.LENGTH_LONG)
                .show()
        }
    }

    private suspend fun checkForUpdates() {
        val context = this
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        lifecycleScope.launch {
            try {
                val isConnected = isOnline(context)
                if (!isConnected) {
                    return@launch
                }
                val response = RetrofitInstance.githubApiService.getLatestVersion()
                if (response.isSuccessful && response.body() != null) {
                    val githubLatestResponse: GithubLatestResponse = response.body()!!
                    db?.let {
                        it.settingDao.insert(
                            Setting(
                                SettingDao.LATEST_CHANGELOG_ID,
                                if (githubLatestResponse.body != null) githubLatestResponse.body else SettingDao.DEFAULT_LATEST_CHANGELOG
                            )
                        )
                        if (!packageInfo.versionName.equals(githubLatestResponse.tag_name.drop(1))) {
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_AVAILABLE_ID,
                                    githubLatestResponse.tag_name
                                )
                            )
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_PUBLISH_DATE_ID,
                                    githubLatestResponse.published_at
                                )
                            )
                            val latestDownloadUrl =
                                githubLatestResponse.assets?.get(0)?.browser_download_url
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_DOWNLOAD_URL_ID,
                                    if (latestDownloadUrl != null) latestDownloadUrl else SettingDao.DEFAULT_LATEST_DOWNLOAD_URL
                                )
                            )
                        } else {
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_AVAILABLE_ID,
                                    SettingDao.DEFAULT_LATEST_AVAILABLE
                                )
                            )
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_PUBLISH_DATE_ID,
                                    SettingDao.DEFAULT_LATEST_PUBLISH_DATE
                                )
                            )
                            it.settingDao.insert(
                                Setting(
                                    SettingDao.LATEST_DOWNLOAD_URL_ID,
                                    SettingDao.DEFAULT_LATEST_DOWNLOAD_URL
                                )
                            )
                        }
                    }
                }
            } catch (ioException: IOException) {
                Log.e("ToolViewModel", "IOException while checking for updates")
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork != null && connectivityManager.getNetworkCapabilities(activeNetwork) != null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val stickingPointsNotificationChannel = NotificationChannel(
                NotificationService.STICKING_POINT_NOTIFICATION_CHANNEL_ID,
                NotificationService.STICKING_POINT_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            stickingPointsNotificationChannel.description = "Daygame sticking point notification"
            val liveSessionNotificationChannel = NotificationChannel(
                NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_ID,
                NotificationService.LIVE_SESSION_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            liveSessionNotificationChannel.description = "Live session persistent notification"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(stickingPointsNotificationChannel)
            notificationManager.createNotificationChannel(liveSessionNotificationChannel)
        }
    }

    private val requestContactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Contact permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Contact permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestLegacyPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.values.all { it }
        if (allGranted) {
            Toast.makeText(this, "Storage permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Storage permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

}