package com.example.projectses4_apptivi.model

class VideoModel {
    var id:String ?= null
    var user:String ?= null
    var title:String ?= null
    var timeStamp:String ?= null
    var videouri:String ?= null
    constructor(){
        //firebase required empty constructor
    }

    constructor(id: String?, user: String?, title: String?, timeStamp: String?, videouri: String?) {
        this.id = id
        this.user = user
        this.title = title
        this.timeStamp = timeStamp
        this.videouri = videouri
    }

}