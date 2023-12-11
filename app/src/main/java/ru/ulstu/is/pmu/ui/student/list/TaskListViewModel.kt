package ru.ulstu.`is`.pmu.ui.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.ulstu.`is`.pmu.database.AppDataContainer
import ru.ulstu.`is`.pmu.database.task.model.Task
import ru.ulstu.`is`.pmu.database.task.repository.TaskRepository

class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val taskListFavoriteUiState: StateFlow<TaskListUiState> = taskRepository.getAllTasks()
        .map { tasks ->
            val filteredTasks = tasks.filter { task ->
                task.favorite // Оставить только задачи, у которых favorite = true
            }
            val sortedTasks = filteredTasks.sortedByDescending { task ->
                parseDate(task.endDate)?.timeInMillis ?: Long.MIN_VALUE
            }
            TaskListUiState(sortedTasks)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(AppDataContainer.TIMEOUT), TaskListUiState())

    // Для TaskListEndDate (Сортировка по endDate)
    val taskListEndDateUiState: StateFlow<TaskListUiState> = taskRepository.getAllTasks()
        .map { tasks ->
            val sortedTasks = tasks.sortedByDescending { task ->
                parseDate(task.endDate)?.timeInMillis ?: Long.MIN_VALUE
            }
            TaskListUiState(sortedTasks)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(AppDataContainer.TIMEOUT), TaskListUiState())

    // Для TaskList (Сортировка по uid)
    val taskListUiState: StateFlow<TaskListUiState> = taskRepository.getAllTasks()
        .map { tasks ->
            val sortedTasks = tasks.sortedBy { task ->
                task.uid
            }
            TaskListUiState(sortedTasks)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(AppDataContainer.TIMEOUT), TaskListUiState())

    suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }
    suspend fun favoriteTask(task: Task) {
        taskRepository.setTaskFavorite(task.uid, true)
    }
    suspend fun deletefavoriteTask(task: Task) {
        taskRepository.setTaskFavorite(task.uid, false)
    }
}

data class TaskListUiState(val taskList: List<Task> = listOf())