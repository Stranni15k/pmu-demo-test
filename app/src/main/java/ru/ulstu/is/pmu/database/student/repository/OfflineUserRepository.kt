package ru.ulstu.`is`.pmu.database.task.repository

import ru.ulstu.`is`.pmu.database.task.dao.UserDao
import ru.ulstu.`is`.pmu.database.task.model.User

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override suspend fun getAllUsers(): List<User> = userDao.getAll()
}