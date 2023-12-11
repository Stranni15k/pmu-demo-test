package ru.ulstu.`is`.pmu.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import ru.ulstu.`is`.pmu.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector = Icons.Filled.Favorite,
    val showInBottomBar: Boolean = true
) {
    StudentList(
        "student-list", R.string.student_main_title, Icons.Filled.List
    ),
    About(
        "about", R.string.about_title, Icons.Filled.Info
    ),
    StudentEdit(
        "student-edit/{id}", R.string.student_view_title, showInBottomBar = false
    );

    companion object {
        val bottomBarItems = listOf(
            StudentList,
            About,
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}