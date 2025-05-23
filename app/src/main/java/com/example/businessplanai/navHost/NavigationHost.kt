package com.example.businessplanai.navHost

import EditViewModel
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
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.routes.ScreenRoute
import com.example.businessplanai.screens.AddPlan
import com.example.businessplanai.screens.EditPlan
import com.example.businessplanai.screens.Main
import com.example.businessplanai.screens.NotesAdaptiveScreen
import com.example.businessplanai.screens.Watch

import com.example.businessplanai.viewModel.AddViewModel
import com.example.businessplanai.viewModel.MainViewModel
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
    db: AppDatabase,
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
                db = db,
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
                db
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
                db,
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
                db,
                id,
                listState
            )
        }
        composable(ScreenRoute.Adaptive.route) {
            NotesAdaptiveScreen(
                padding,
                mainViewModel,
                watchViewModel,
                db,
                navigation,
                listState
            )
        }
    }
}