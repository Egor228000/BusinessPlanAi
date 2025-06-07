package com.example.businessplanai

import android.app.Activity
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.businessplanai.navDisplay.AddScreenNav
import com.example.businessplanai.navDisplay.MainScreenNav
import com.example.businessplanai.navDisplay.NavigationHost
import com.example.businessplanai.navDisplay.WatchScreenNav
import com.example.businessplanai.ui.theme.BusinessPlanAITheme
import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.EditViewModel
import com.example.businessplanai.viewModel.MainViewModel
import com.example.businessplanai.viewModel.SettingViewModel
import com.example.businessplanai.viewModel.WatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(
        ExperimentalMaterial3Api::class,
        ExperimentalLayoutApi::class,
        ExperimentalMaterial3WindowSizeClassApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val activity = LocalContext.current

            // Навигация
            val backStack = rememberNavBackStack(MainScreenNav)

            val settingViewModel: SettingViewModel = hiltViewModel()
            val mainViewModel: MainViewModel = hiltViewModel()
            val addViewModel: AddViewModel = hiltViewModel()
            val editViewModel: EditViewModel = hiltViewModel()
            val watchViewModel: WatchViewModel = hiltViewModel()

            // Управление темой
            val theme = settingViewModel.appTheme.collectAsState()
            val scope = rememberCoroutineScope()

            // Скрывать кнопку при прокрутки
            val listState = rememberLazyListState()
            LaunchedEffect(Unit) {
                mainViewModel.globalScrollState["main"] = listState
            }
            var lastScrollOffset by remember { mutableIntStateOf(1) }
            LaunchedEffect(listState) {
                snapshotFlow { listState.firstVisibleItemScrollOffset }.collect { offset ->
                    mainViewModel.onScroll(offset - lastScrollOffset.toFloat())
                    lastScrollOffset = offset
                }
            }

            // Показывать кнопку при определенном размере экрана
            val windowSizeClass = calculateWindowSizeClass(activity as Activity)
            var state = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> false
                WindowWidthSizeClass.Medium -> true
                else -> true
            }

            theme.value?.let { actualTheme ->
                BusinessPlanAITheme(
                    appTheme = actualTheme
                ) {
                    Scaffold(
                        contentWindowInsets = WindowInsets(),
                        modifier = Modifier
                            .imePadding(),
                        floatingActionButton = {
                            val fabVisible by mainViewModel.fabVisible.collectAsState()
                            when (backStack.lastOrNull()) {
                                is MainScreenNav -> {
                                    FloatingActionButtonCustom(
                                        onNavigateAdd = { backStack.add(AddScreenNav) },
                                        fabVisible = fabVisible
                                    )
                                }

                                is WatchScreenNav -> {
                                    if (state) {
                                        FloatingActionButtonCustom(
                                            onNavigateAdd = { backStack.add(AddScreenNav) },
                                            fabVisible = fabVisible
                                        )
                                    }
                                }

                                else -> {}
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ) { padding ->
                        NavigationHost(
                            padding,
                            mainViewModel,
                            addViewModel,
                            editViewModel,
                            watchViewModel,
                            settingViewModel,
                            listState,
                            backStack,
                            scope,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FloatingActionButtonCustom(
    onNavigateAdd: () -> Unit,
    fabVisible: Boolean
) {
    AnimatedVisibility(
        visible = fabVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }) {
        FloatingActionButton(
            onClick = onNavigateAdd,
            containerColor = MaterialTheme.colorScheme.onSurface,
            elevation = FloatingActionButtonDefaults.elevation(1.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
            )
        }
    }
}


