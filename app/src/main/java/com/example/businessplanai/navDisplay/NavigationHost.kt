package com.example.businessplanai.navDisplay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.navEntryDecorator
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.businessplanai.screens.AddPlan
import com.example.businessplanai.screens.EditPlan
import com.example.businessplanai.screens.Main
import com.example.businessplanai.screens.Settings
import com.example.businessplanai.screens.Watch
import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.EditViewModel
import com.example.businessplanai.viewModel.MainViewModel
import com.example.businessplanai.viewModel.SettingViewModel
import com.example.businessplanai.viewModel.WatchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable


@Serializable
data object MainScreenNav : NavKey

@Serializable
data object AddScreenNav : NavKey

@Serializable
data class WatchScreenNav(val id: Int = 0) : NavKey

@Serializable
data class EditScreenNav(val id: Int = 0) : NavKey

@Serializable
data object SettingScreenNav : NavKey


@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationHost(
    padding: PaddingValues,
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel,
    editViewModel: EditViewModel,
    watchViewModel: WatchViewModel,
    settingViewModel: SettingViewModel,
    listState: LazyListState,
    backStack: NavBackStack,
    scope: CoroutineScope,
) {

    val localNavSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
        compositionLocalOf {
            throw IllegalStateException(
                "Unexpected access to LocalNavSharedTransitionScope. You must provide a " +
                        "SharedTransitionScope from a call to SharedTransitionLayout() or " +
                        "SharedTransitionScope()"
            )
        }

    val sharedEntryInSceneNavEntryDecorator = navEntryDecorator { entry ->
        with(localNavSharedTransitionScope.current) {
            Box(
                Modifier
                    .sharedElement(
                        rememberSharedContentState(entry.key),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    ),
            ) {
                entry.content(entry.key)
            }
        }
    }

    val twoPaneStrategy = remember { TwoPaneSceneStrategy<Any>() }

    SharedTransitionLayout {
        CompositionLocalProvider(localNavSharedTransitionScope provides this) {
            NavDisplay(
                modifier = Modifier.padding(padding),
                backStack = backStack,
                onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
                entryDecorators = listOf(
                    sharedEntryInSceneNavEntryDecorator,
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator()
                ),
                sceneStrategy = twoPaneStrategy,
                entryProvider = entryProvider {
                    entry<MainScreenNav>(
                        metadata = TwoPaneScene.twoPane()

                    ) { id ->
                        Main(
                            mainViewModel,
                            listState,
                            onNavigateWatch = { id ->
                                if (backStack.lastOrNull() is WatchScreenNav) {
                                    backStack.removeLastOrNull()
                                }
                                backStack.add(WatchScreenNav(id))
                            },
                            onEditClick = { id ->
                                backStack.add(EditScreenNav(id = id))
                            },
                            onDeleteClick = { id ->
                                mainViewModel.deleteBusiness(id = id)
                            },
                            onNavigateSetting = { backStack.add(SettingScreenNav) },

                        )

                    }
                    entry<WatchScreenNav>(
                        metadata = TwoPaneScene.twoPane()

                    ) { id ->
                        Watch(
                            watchViewModel,
                            id.id,
                            listState,
                            onBack = {
                                backStack.removeLastOrNull()
                            },
                            scope
                        )
                    }

                    entry<EditScreenNav>(

                    ) { id ->
                        EditPlan(
                            editViewModel,
                            id.id,
                            scope,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }

                    entry<SettingScreenNav>() {

                        Settings(
                            settingViewModel,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }

                    entry<AddScreenNav>() {

                        AddPlan(
                            addViewModel,
                            settingViewModel,
                            onBack = {
                                backStack.removeLastOrNull()

                            }
                        )
                    }
                }
            )
        }
    }
}


