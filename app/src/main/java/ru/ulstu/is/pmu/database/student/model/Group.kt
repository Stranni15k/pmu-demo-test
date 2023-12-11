package ru.ulstu.`is`.pmu.database.student.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "group_name")
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Group
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid
    }

    companion object {
        val DEMO_GROUP = Group(
            0,
            "Группа 1"
        )
    }
}
