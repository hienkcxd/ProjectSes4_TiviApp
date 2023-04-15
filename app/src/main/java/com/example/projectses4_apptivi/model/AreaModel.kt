package com.example.projectses4_apptivi.model

class AreaModel {
    var id:String ?= null
    var areaName:String ?= null
    var userName:String ?= null

    constructor(){}
    constructor(areaName: String?, userName: String?) {
        this.areaName = areaName
        this.userName = userName
    }

}