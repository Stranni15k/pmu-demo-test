package ru.ulstu.`is`.pmu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulstu.`is`.pmu.database.student.dao.GroupDao
import ru.ulstu.`is`.pmu.database.student.dao.StudentDao
import ru.ulstu.`is`.pmu.database.student.model.Group
import ru.ulstu.`is`.pmu.database.student.model.Student

@Database(entities = [Student::class, Group::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun groupDao(): GroupDao

    companion object {
        private const val DB_NAME: String = "pmy-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private suspend fun populateDatabase() {
            INSTANCE?.let { database ->
                // Groups
                val groupDao = database.groupDao()
                val group1 = Group(1, "Группа 1")
                val group2 = Group(2, "Группа 2")
                val group3 = Group(3, "Группа 3")
                groupDao.insert(group1)
                groupDao.insert(group2)
                groupDao.insert(group3)
                // Students
                val studentDao = database.studentDao()
                val student1 = Student("First1", "Last1", group1, "+79998887761", "st1@m.ru")
                val student2 = Student("First2", "Last2", group2, "+79998887762", "st2@m.ru")
                val student3 = Student("First3", "Last3", group3, "+79998887763", "st3@m.ru")
                val student4 = Student("First4", "Last4", group3, "+79998887764", "st4@m.ru")
                val student5 = Student("First5", "Last5", group2, "+79998887765", "st5@m.ru")
                studentDao.insert(student1)
                studentDao.insert(student2)
                studentDao.insert(student3)
                studentDao.insert(student4)
                studentDao.insert(student5)
            }
        }

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDatabase()
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}