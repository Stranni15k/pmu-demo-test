package ru.ulstu.`is`.pmu.database.task.repository

import ru.ulstu.`is`.pmu.database.task.dao.UserDao
import ru.ulstu.`is`.pmu.database.task.model.User

interface UserRepository {
    suspend fun getAllUsers(): List<User>
}