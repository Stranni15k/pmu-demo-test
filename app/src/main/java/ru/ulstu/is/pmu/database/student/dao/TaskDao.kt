package ru.ulstu.`is`.pmu.database.task.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.task.model.Task

@Dao
interface TaskDao {
    @Query("select * from tasks order by name collate nocase asc")
    fun getAll(): Flow<List<Task>>

    @Query("select * from tasks where tasks.uid = :uid")
    fun getByUid(uid: Int): Flow<Task>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("UPDATE tasks SET favorite = :favorite WHERE uid = :uid")
    suspend fun updateFavorite(uid: Int, favorite: Boolean)
}