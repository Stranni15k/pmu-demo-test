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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
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
fun TaskListFavorite(
    navController: NavController,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val taskListFavoriteUiState by viewModel.taskListFavoriteUiState.collectAsState()
    Scaffold(
        topBar = {}
    ) { innerPadding ->
        TaskListFavorite(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            taskList = taskListFavoriteUiState.taskList,
            onClick = { uid: Int ->
                val route = Screen.TaskEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onSwipe = { task: Task ->
                coroutineScope.launch {
                    viewModel.deletefavoriteTask(task)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    task: Task
) {
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        background = {
            val backgroundColor by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                    else -> Color.White
                }, label = ""
            )
            val iconScale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissedToStart) 1.3f else 0.5f,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier.scale(iconScale),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TaskListItem(task = task,
                modifier = Modifier
                    .padding(vertical = 7.dp))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListFavorite(
    modifier: Modifier = Modifier,
    taskList: List<Task>,
    onClick: (uid: Int) -> Unit,
    onSwipe: (task: Task) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.task_empty_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
                items(items = taskList, key = { it.uid }) { task ->
                    val dismissState: DismissState = rememberDismissState(
                        positionalThreshold = { 200.dp.toPx() }
                    )

                    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                        onSwipe(task)
                    }

                    SwipeToDelete(
                        dismissState = dismissState,
                        task = task
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