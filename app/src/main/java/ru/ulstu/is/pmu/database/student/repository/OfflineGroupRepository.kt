package ru.ulstu.`is`.pmu.database.student.repository

import ru.ulstu.`is`.pmu.database.student.dao.GroupDao
import ru.ulstu.`is`.pmu.database.student.model.Group

class OfflineGroupRepository(private val groupDao: GroupDao) : GroupRepository {
    override suspend fun getAllGroups(): List<Group> = groupDao.getAll()
}