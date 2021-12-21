package com.example.android.todolistapplication.network

data class Location(
    val name: String,
    val region: String,
    var country: String,
    var lat: Double,
    var lon: Double,
    var tz_id: String,
    var localtime_epoch: Long,
    var localtime: String
)
