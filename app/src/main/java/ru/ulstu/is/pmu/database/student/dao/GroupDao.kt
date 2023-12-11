package ru.ulstu.`is`.pmu.database.student.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.ulstu.`is`.pmu.database.student.model.Group

@Dao
interface GroupDao {
    @Query("select * from groups order by group_name collate nocase asc")
    suspend fun getAll(): List<Group>

    @Insert
    suspend fun insert(group: Group)

    @Update
    suspend fun update(group: Group)

    @Delete
    suspend fun delete(group: Group)
}