package ru.ulstu.`is`.pmu.ui.student.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ulstu.`is`.pmu.database.student.model.Student
import ru.ulstu.`is`.pmu.database.student.repository.StudentRepository

class StudentEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val studentRepository: StudentRepository
) : ViewModel() {

    var studentUiState by mutableStateOf(StudentUiState())
        private set

    private val studentUid: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            if (studentUid > 0) {
                studentUiState = studentRepository.getStudent(studentUid)
                    .filterNotNull()
                    .first()
                    .toUiState(true)
            }
        }
    }

    fun updateUiState(studentDetails: StudentDetails) {
        studentUiState = StudentUiState(
            studentDetails = studentDetails,
            isEntryValid = validateInput(studentDetails)
        )
    }

    suspend fun saveStudent() {
        if (validateInput()) {
            if (studentUid > 0) {
                studentRepository.updateStudent(studentUiState.studentDetails.toStudent(studentUid))
            } else {
                studentRepository.insertStudent(studentUiState.studentDetails.toStudent())
            }
        }
    }

    private fun validateInput(uiState: StudentDetails = studentUiState.studentDetails): Boolean {
        return with(uiState) {
            firstName.isNotBlank()
                    && lastName.isNotBlank()
                    && phone.isNotBlank()
                    && email.isNotBlank()
                    && groupId > 0
        }
    }
}

data class StudentUiState(
    val studentDetails: StudentDetails = StudentDetails(),
    val isEntryValid: Boolean = false
)

data class StudentDetails(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val groupId: Int = 0
)

fun StudentDetails.toStudent(uid: Int = 0): Student = Student(
    uid = uid,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    groupId = groupId
)

fun Student.toDetails(): StudentDetails = StudentDetails(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    groupId = groupId
)

fun Student.toUiState(isEntryValid: Boolean = false): StudentUiState = StudentUiState(
    studentDetails = this.toDetails(),
    isEntryValid = isEntryValid
)