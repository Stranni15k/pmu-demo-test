package ru.ulstu.`is`.pmu.database.task.repository

import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.task.model.Task

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTask(uid: Int): Flow<Task?>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun setTaskFavorite(uid: Int, favorite: Boolean = true)
}