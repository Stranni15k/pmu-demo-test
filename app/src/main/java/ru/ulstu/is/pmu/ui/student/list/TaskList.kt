package ru.ulstu.`is`.pmu.ui.task.list

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DismissValue.DismissedToStart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.ulstu.`is`.pmu.R
import ru.ulstu.`is`.pmu.database.task.model.Task
import ru.ulstu.`is`.pmu.ui.AppViewModelProvider
import ru.ulstu.`is`.pmu.ui.navigation.Screen
import ru.ulstu.`is`.pmu.ui.theme.PmudemoTheme

@Composable
fun TaskList(
    navController: NavController,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val taskListUiState by viewModel.taskListUiState.collectAsState()
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.TaskEdit.route.replace("{id}", "0")
                    navController.navigate(route)
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
    ) { innerPadding ->
        TaskListContent( // Изменил имя, чтобы избежать путаницы с функцией TaskList
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            taskList = taskListUiState.taskList,
            onClick = { uid: Int ->
                val route = Screen.TaskEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onSwipeLeft = { task: Task ->  // Обработка свайпа влево (удаление)
                coroutineScope.launch {
                    viewModel.deleteTask(task)
                }
            },
            onSwipeRight = { task: Task ->  // Обработка свайпа вправо (добавить в избранное)
                coroutineScope.launch {
                    viewModel.favoriteTask(task)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToAction(
    dismissState: DismissState,
    task: Task,
    onClick: (uid: Int) -> Unit,
    onSwipeRight: (task: Task) -> Unit, // Для добавления в избранное
    onSwipeLeft: (task: Task) -> Unit // Для удаления
) {
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart, // Для удаления
            DismissDirection.StartToEnd  // Для добавления в избранное
        ),
        background = {
            val backgroundColor by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                    DismissValue.DismissedToEnd -> Color.Green.copy(alpha = 0.8f) // Цвет фона для избранного
                    else -> Color.White
                }, label = ""
            )
            val iconScale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissedToStart || dismissState.targetValue == DismissValue.DismissedToEnd) 1.3f else 0.5f,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
                    .padding(end = 16.dp, start = 16.dp),
                contentAlignment = if (dismissState.targetValue == DismissedToStart) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Icon(
                    modifier = Modifier.scale(iconScale),
                    imageVector = if (dismissState.targetValue == DismissedToStart) Icons.Outlined.Delete else Icons.Default.Star, // Иконка для избранного
                    contentDescription = if (dismissState.targetValue == DismissedToStart) "Delete" else "Favorite",
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TaskListItem(task = task,
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .clickable { onClick(task.uid) })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListContent(
    modifier: Modifier = Modifier,
    taskList: List<Task>,
    onClick: (uid: Int) -> Unit,
    onSwipeLeft: (task: Task) -> Unit,
    onSwipeRight: (task: Task) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            // Код для отображения пустого списка
        } else {
            LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
                items(items = taskList, key = { it.uid }) { task ->
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        LaunchedEffect(task) {
                            onSwipeLeft(task)
                            dismissState.reset() // Сбросить состояние после обработки свайпа
                        }
                    }

                    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                        LaunchedEffect(task) {
                            onSwipeRight(task)
                            dismissState.reset() // Сбросить состояние после обработки свайпа
                        }
                    }

                    SwipeToAction(
                        dismissState = dismissState,
                        task = task,
                        onClick = onClick,
                        onSwipeRight = onSwipeRight,
                        onSwipeLeft = onSwipeLeft
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskListItem(
    task: Task, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = modifier.padding(all = 10.dp)
        ) {
            Text(
                text = String.format("%s%n%s", task.name, task.description)
            )
        }
    }
}