package ru.ulstu.`is`.pmu.ui.student.list

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
import ru.ulstu.`is`.pmu.database.student.model.Student
import ru.ulstu.`is`.pmu.ui.AppViewModelProvider
import ru.ulstu.`is`.pmu.ui.navigation.Screen
import ru.ulstu.`is`.pmu.ui.theme.PmudemoTheme

@Composable
fun StudentList(
    navController: NavController,
    viewModel: StudentListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val studentListUiState by viewModel.studentListUiState.collectAsState()
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.StudentEdit.route.replace("{id}", 0.toString())
                    navController.navigate(route)
                },
            ) {
                Icon(Icons.Filled.Add, "Добавить")
            }
        }
    ) { innerPadding ->
        StudentList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            studentList = studentListUiState.studentList,
            onClick = { uid: Int ->
                val route = Screen.StudentEdit.route.replace("{id}", uid.toString())
                navController.navigate(route)
            },
            onSwipe = { student: Student ->
                coroutineScope.launch {
                    viewModel.deleteStudent(student)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    student: Student,
    onClick: (uid: Int) -> Unit
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
            StudentListItem(student = student,
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .clickable { onClick(student.uid) })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentList(
    modifier: Modifier = Modifier,
    studentList: List<Student>,
    onClick: (uid: Int) -> Unit,
    onSwipe: (student: Student) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (studentList.isEmpty()) {
            Text(
                text = stringResource(R.string.student_empty_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
                items(items = studentList, key = { it.uid }) { student ->
                    val dismissState: DismissState = rememberDismissState(
                        positionalThreshold = { 200.dp.toPx() }
                    )

                    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                        onSwipe(student)
                    }

                    SwipeToDelete(
                        dismissState = dismissState,
                        student = student,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun StudentListItem(
    student: Student, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = modifier.padding(all = 10.dp)
        ) {
            Text(
                text = String.format("%s %s", student.firstName, student.lastName)
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentListPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            StudentList(
                studentList = (1..20).map { i -> Student.getStudent(i) },
                onClick = {},
                onSwipe = {}
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentEmptyListPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            StudentList(
                studentList = listOf(),
                onClick = {},
                onSwipe = {}
            )
        }
    }
}