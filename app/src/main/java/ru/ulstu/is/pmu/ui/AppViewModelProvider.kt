package ru.ulstu.`is`.pmu.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.ulstu.`is`.pmu.TaskApplication
import ru.ulstu.`is`.pmu.ui.task.edit.UserDropDownViewModel
import ru.ulstu.`is`.pmu.ui.task.edit.TaskEditViewModel
import ru.ulstu.`is`.pmu.ui.task.list.TaskListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskListViewModel(taskApplication().container.taskRepository)
        }
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                taskApplication().container.taskRepository
            )
        }
        initializer {
            UserDropDownViewModel(taskApplication().container.userRepository)
        }
    }
}

fun CreationExtras.taskApplication(): TaskApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)