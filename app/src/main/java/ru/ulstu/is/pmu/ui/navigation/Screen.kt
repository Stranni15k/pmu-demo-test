package ru.ulstu.`is`.pmu.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import ru.ulstu.`is`.pmu.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector = Icons.Filled.Favorite,
    val showInBottomBar: Boolean = true
) {
    TaskList(
        "task-list", R.string.task_main_title, Icons.Filled.List
    ),
    TaskListEndDate(
        "task-list-end-date", R.string.task_date_title, Icons.Filled.DateRange
    ),
    TaskListFavorite(
        "task-list-favorite", R.string.task_favorite, Icons.Filled.Favorite
    ),
    About(
        "about", R.string.about_title, Icons.Filled.Info
    ),
    TaskEdit(
        "task-edit/{id}", R.string.task_view_title, showInBottomBar = false
    );

    companion object {
        val bottomBarItems = listOf(
            TaskList,
            TaskListEndDate,
            TaskListFavorite,
            About,
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}