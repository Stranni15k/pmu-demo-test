package ru.ulstu.`is`.pmu.ui.student.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.ulstu.`is`.pmu.database.AppDataContainer
import ru.ulstu.`is`.pmu.database.student.model.Student
import ru.ulstu.`is`.pmu.database.student.repository.StudentRepository

class StudentListViewModel(
    private val studentRepository: StudentRepository
) : ViewModel() {
    val studentListUiState: StateFlow<StudentListUiState> = studentRepository.getAllStudents().map {
        StudentListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = StudentListUiState()
    )

    suspend fun deleteStudent(student: Student) {
        studentRepository.deleteStudent(student)
    }
}

data class StudentListUiState(val studentList: List<Student> = listOf())