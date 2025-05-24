package com.example.businessplanai.navHost

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.businessplanai.routes.ScreenRoute
import com.example.businessplanai.screens.AddPlan
import com.example.businessplanai.screens.EditPlan
import com.example.businessplanai.screens.Main
import com.example.businessplanai.screens.NotesAdaptiveScreen
import com.example.businessplanai.screens.Settings
import com.example.businessplanai.screens.Watch
import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.EditViewModel
import com.example.businessplanai.viewModel.MainViewModel
import com.example.businessplanai.viewModel.SettingViewModel
import com.example.businessplanai.viewModel.WatchViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationHost(
    navigation: NavHostController,
    padding: PaddingValues,
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel,
    editViewModel: EditViewModel,
    watchViewModel: WatchViewModel,
    settingViewModel: SettingViewModel,
    listState: LazyListState
) {
    NavHost(
        navController = navigation,
        startDestination = ScreenRoute.Adaptive.route
    ) {

        composable(ScreenRoute.Main.route) {
            Main(
                padding = padding,
                mainViewModel = mainViewModel,
                navigation = navigation,
                onNoteSelected = { id ->
                    navigation.navigate("${ScreenRoute.Watch.route}/$id")
                },
                onDeleteClick = { id ->
                    mainViewModel.deleteBusiness(id)
                },
                listState = listState
            )
        }
        composable(ScreenRoute.Add.route) {
            AddPlan(
                padding,
                addViewModel,
                navigation,
            )
        }

        composable(
       "${ScreenRoute.Edit.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")?.toInt()
            EditPlan(
                padding,
                editViewModel,
                navigation,
                id
            )
        }
        composable(
          "${ScreenRoute.Watch.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")?.toInt()
            Watch(
                padding,
                watchViewModel,
                navigation,
                id,
                listState
            )
        }
        composable(ScreenRoute.Adaptive.route) {
            NotesAdaptiveScreen(
                padding,
                mainViewModel,
                watchViewModel,
                navigation,
                listState
            )
        }

        composable(ScreenRoute.Settings.route) {
            Settings(
                navigation,
                padding,
                settingViewModel
            )
        }
    }
}