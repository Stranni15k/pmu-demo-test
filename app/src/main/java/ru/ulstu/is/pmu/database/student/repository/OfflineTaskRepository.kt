package ru.ulstu.`is`.pmu.database.task.repository

import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.task.dao.TaskDao
import ru.ulstu.`is`.pmu.database.task.model.Task

class OfflineTaskRepository(private val taskDao: TaskDao) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()

    override fun getTask(uid: Int): Flow<Task?> = taskDao.getByUid(uid)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun setTaskFavorite(uid: Int, favorite: Boolean) {
        taskDao.updateFavorite(uid, favorite)
    }
}