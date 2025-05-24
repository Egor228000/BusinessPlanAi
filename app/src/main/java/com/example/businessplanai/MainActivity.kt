package com.example.businessplanai

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.businessplanai.navHost.NavigationHost
import com.example.businessplanai.routes.ScreenRoute
import com.example.businessplanai.ui.theme.BusinessPlanAITheme
import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.EditViewModel
import com.example.businessplanai.viewModel.MainViewModel
import com.example.businessplanai.viewModel.SettingViewModel
import com.example.businessplanai.viewModel.WatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
        ExperimentalMaterial3WindowSizeClassApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val addViewModel: AddViewModel = hiltViewModel()
            val editViewModel: EditViewModel = hiltViewModel()
            val watchViewModel: WatchViewModel = hiltViewModel()
            val settingViewModel: SettingViewModel = hiltViewModel()




            val context = LocalContext.current.applicationContext as Application

            val scope = rememberCoroutineScope()

            val navigation = rememberNavController()
            val navBackStackEntry by navigation.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            var expanded by remember { mutableStateOf(false) }
            val listState = rememberLazyListState()

            LaunchedEffect(Unit) {
                mainViewModel.globalScrollState["main"] = listState
            }
            var lastScrollOffset by remember { mutableIntStateOf(1) }

            LaunchedEffect(listState) {
                snapshotFlow { listState.firstVisibleItemScrollOffset }
                    .collect { offset ->
                        mainViewModel.onScroll(offset - lastScrollOffset.toFloat())
                        lastScrollOffset = offset
                    }
            }
            val theme = settingViewModel.appTheme.collectAsState()
            BusinessPlanAITheme(
                appTheme = theme.value
            ) {
                Scaffold(
                    modifier = Modifier.imePadding(),
                    floatingActionButton = {
                        val fabVisible by mainViewModel.fabVisible.collectAsState()
                        when (currentRoute) {
                            ScreenRoute.Adaptive.route -> {
                                AnimatedVisibility(
                                    visible = fabVisible,
                                    enter = fadeIn() + slideInVertically { it },
                                    exit = fadeOut() + slideOutVertically { it }
                                ) {
                                    FloatingActionButton(
                                        onClick = { navigation.navigate(ScreenRoute.Add.route) },
                                        containerColor = MaterialTheme.colorScheme.onSurface,
                                        elevation =  FloatingActionButtonDefaults.elevation(1.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.background,
                                        )
                                    }
                                }
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    topBar = {
                        when (currentRoute) {
                            "${ScreenRoute.Edit.route}/{id}" -> {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.onPrimary ),
                                    title = {
                                        Text(
                                            "Редактирование",
                                            color = MaterialTheme.colorScheme.background,
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = { navigation.popBackStack() }
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    },
                                    actions = {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    editViewModel.saveChanges()
                                                }
                                                navigation.popBackStack()
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Done, null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    }
                                )
                            }

                            "${ScreenRoute.Watch.route}/{id}" -> {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.onPrimary ),

                                    title = {
                                        Text(
                                            "Просмотр",
                                            color = MaterialTheme.colorScheme.background,

                                            )
                                    },
                                    navigationIcon = {

                                        IconButton(
                                            onClick = { navigation.popBackStack() }
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    },
                                    actions = {
                                        IconButton(
                                            onClick = {
                                                expanded = true
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.outline_arrow_downward_24),
                                                null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            containerColor = MaterialTheme.colorScheme.onBackground
                                        ) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    scope.launch(Dispatchers.IO) {
                                                        expanded = false

                                                        watchViewModel.saveTextToDownloads(
                                                            context,
                                                            "Бизнес_план.pdf",
                                                            "application/pdf",
                                                            watchViewModel.getCurrentBusinessText()
                                                        )
                                                    }
                                                },
                                                text = { Text("PDF", color = MaterialTheme.colorScheme.background,) },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.Edit,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.background
                                                    )
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    MaterialTheme.colorScheme.onBackground
                                                )
                                            )
                                            DropdownMenuItem(
                                                onClick = {
                                                    scope.launch(Dispatchers.IO) {

                                                        expanded = false
                                                        watchViewModel.saveTextToDownloads(
                                                            context = context,
                                                            fileName = "бизнес_план.docx",
                                                            mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                                            content = watchViewModel.getCurrentBusinessText()
                                                        )
                                                    }
                                                },
                                                text = { Text("Word") },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.background
                                                    )
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    MaterialTheme.colorScheme.background
                                                )
                                            )
                                        }
                                    }
                                )
                            }

                            ScreenRoute.Add.route -> {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.onPrimary ),
                                    title = {
                                        Text(
                                            "Добавление безнесс плана",
                                            color = MaterialTheme.colorScheme.background,
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = { navigation.popBackStack() }
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    }
                                )
                            }
                            ScreenRoute.Settings.route -> {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.onPrimary ),
                                    title = {
                                        Text(
                                            "Настройки",
                                            color = MaterialTheme.colorScheme.background,
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = { navigation.popBackStack() }
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                                tint = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    },
                ) { padding ->
                    NavigationHost(
                        navigation,
                        padding,
                        mainViewModel,
                        addViewModel,
                        editViewModel,
                        watchViewModel,
                        settingViewModel,
                        listState,

                    )
                }
            }
        }
    }
}
