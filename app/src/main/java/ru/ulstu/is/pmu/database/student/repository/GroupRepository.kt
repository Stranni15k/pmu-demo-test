package ru.ulstu.`is`.pmu.database.student.repository

import ru.ulstu.`is`.pmu.database.student.model.Group

interface GroupRepository {
    suspend fun getAllGroups(): List<Group>
}