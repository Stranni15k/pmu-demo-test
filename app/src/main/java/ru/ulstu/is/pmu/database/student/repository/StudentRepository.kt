package ru.ulstu.`is`.pmu.database.student.repository

import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.student.model.Student

interface StudentRepository {
    fun getAllStudents(): Flow<List<Student>>
    fun getStudent(uid: Int): Flow<Student?>
    suspend fun insertStudent(student: Student)
    suspend fun updateStudent(student: Student)
    suspend fun deleteStudent(student: Student)
}