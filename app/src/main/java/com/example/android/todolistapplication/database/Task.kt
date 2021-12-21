package com.example.android.todolistapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "creation_date")
    val creationDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_done")
    var isDone: Boolean = false

) {

    constructor(id: Long, title: String, description: String, isDone: Boolean) : this() {
        this.id = id
        this.title = title
        this.description = description
        this.isDone = isDone
    }

}
