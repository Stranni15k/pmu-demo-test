package ru.ulstu.`is`.pmu.database

import android.content.Context
import ru.ulstu.`is`.pmu.database.student.repository.GroupRepository
import ru.ulstu.`is`.pmu.database.student.repository.OfflineGroupRepository
import ru.ulstu.`is`.pmu.database.student.repository.OfflineStudentRepository
import ru.ulstu.`is`.pmu.database.student.repository.StudentRepository

interface AppContainer {
    val studentRepository: StudentRepository
    val groupRepository: GroupRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val studentRepository: StudentRepository by lazy {
        OfflineStudentRepository(AppDatabase.getInstance(context).studentDao())
    }
    override val groupRepository: GroupRepository by lazy {
        OfflineGroupRepository(AppDatabase.getInstance(context).groupDao())
    }

    companion object {
        const val TIMEOUT = 5000L
    }
}