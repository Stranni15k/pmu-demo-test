package ru.ulstu.`is`.pmu.database.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "user")
    val name: String,
    @ColumnInfo(name = "login")
    val login: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid
    }

    companion object {
        val DEMO_User = User(
            0,
            "Sergey",
            "Serega"
        )
    }
}
