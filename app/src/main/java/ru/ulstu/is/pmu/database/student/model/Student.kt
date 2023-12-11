package ru.ulstu.`is`.pmu.database.student.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "students", foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["uid"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class Student(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "group_id", index = true)
    val groupId: Int,
    val phone: String,
    val email: String
) {

    @Ignore
    constructor(
        firstName: String,
        lastName: String,
        group: Group,
        phone: String,
        email: String
    ) : this(0, firstName, lastName, group.uid, phone, email)


    companion object {
        fun getStudent(index: Int = 0): Student {
            return Student(
                index,
                "first",
                "last",
                0,
                "8 999 777 65 65",
                "email@mail.ru"
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Student
        if (uid != other.uid) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (groupId != other.groupId) return false
        if (phone != other.phone) return false
        if (email != other.email) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + groupId
        result = 31 * result + phone.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}
