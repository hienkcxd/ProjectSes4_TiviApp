package com.example.projectses4_apptivi.model

data class DetailDeviceJson(
    val area: String,
    val deviceID: String,
    val deviceName: String,
    val fileName: List<String>,
    val fileStorage: List<FileStorage>,
    val groupName: String,
    val id: Int,
    val storeName: String,
    val username: String
)