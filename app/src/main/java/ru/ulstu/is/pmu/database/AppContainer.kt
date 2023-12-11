package ru.ulstu.`is`.pmu.database

import android.content.Context
import ru.ulstu.`is`.pmu.database.task.repository.UserRepository
import ru.ulstu.`is`.pmu.database.task.repository.OfflineUserRepository
import ru.ulstu.`is`.pmu.database.task.repository.OfflineTaskRepository
import ru.ulstu.`is`.pmu.database.task.repository.TaskRepository

interface AppContainer {
    val taskRepository: TaskRepository
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val taskRepository: TaskRepository by lazy {
        OfflineTaskRepository(AppDatabase.getInstance(context).taskDao())
    }
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }

    companion object {
        const val TIMEOUT = 5000L
    }
}