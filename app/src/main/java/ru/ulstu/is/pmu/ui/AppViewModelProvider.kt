package ru.ulstu.`is`.pmu.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.ulstu.`is`.pmu.StudentApplication
import ru.ulstu.`is`.pmu.ui.student.edit.GroupDropDownViewModel
import ru.ulstu.`is`.pmu.ui.student.edit.StudentEditViewModel
import ru.ulstu.`is`.pmu.ui.student.list.StudentListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            StudentListViewModel(studentApplication().container.studentRepository)
        }
        initializer {
            StudentEditViewModel(
                this.createSavedStateHandle(),
                studentApplication().container.studentRepository
            )
        }
        initializer {
            GroupDropDownViewModel(studentApplication().container.groupRepository)
        }
    }
}

fun CreationExtras.studentApplication(): StudentApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StudentApplication)