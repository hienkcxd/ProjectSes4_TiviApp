package com.example.projectses4_apptivi.model

data class FileStorage(
    val createDate: Int,
    val fileName: String,
    val fileType: String,
    val id: Int,
    val urlGoogleDrive: String,
    val username: String
)