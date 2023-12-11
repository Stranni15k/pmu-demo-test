package ru.ulstu.`is`.pmu.database.student.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.ulstu.`is`.pmu.database.student.model.Student

@Dao
interface StudentDao {
    @Query("select * from students order by last_name collate nocase asc")
    fun getAll(): Flow<List<Student>>

    @Query("select * from students where students.uid = :uid")
    fun getByUid(uid: Int): Flow<Student>

    @Insert
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)
}