package com.example.android.todolistapplication

import java.text.SimpleDateFormat
import java.util.*

fun convertCreationDateToString(creationDate: Long): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(creationDate)
}

fun convertCreationDateToStringWithLabel(creationDate: Long, label: String): String {
    return label + " " + SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(creationDate)
}
