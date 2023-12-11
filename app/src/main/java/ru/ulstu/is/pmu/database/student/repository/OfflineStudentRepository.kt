package ru.ulstu.`is`.pmu.database.student.repository

import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.student.dao.StudentDao
import ru.ulstu.`is`.pmu.database.student.model.Student

class OfflineStudentRepository(private val studentDao: StudentDao) : StudentRepository {
    override fun getAllStudents(): Flow<List<Student>> = studentDao.getAll()

    override fun getStudent(uid: Int): Flow<Student?> = studentDao.getByUid(uid)

    override suspend fun insertStudent(student: Student) = studentDao.insert(student)

    override suspend fun updateStudent(student: Student) = studentDao.update(student)

    override suspend fun deleteStudent(student: Student) = studentDao.delete(student)
}