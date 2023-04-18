package com.example.projectses4_apptivi.model

class VideoModel {
    var id:String ?= null
    var fileName:String ?= null
    var userName:String ?= null
    var fileType:String = "video"
    var createDate:String ?= "coming_soon"
    var urlGoogleDrive:String ?= null
    constructor(){
        //firebase required empty constructor
    }

    constructor(user: String?, fileName: String?, times: String?, urlGoogleDrive: String?) {
        this.userName = user
        this.fileName = fileName
        this.createDate = times
        this.urlGoogleDrive = urlGoogleDrive
    }


}