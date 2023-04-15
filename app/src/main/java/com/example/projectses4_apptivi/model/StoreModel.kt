package com.example.projectses4_apptivi.model

class StoreModel {
    var id:String ?= null
    var storeName:String ?= null
    var userName:String ?= null

    constructor(){}
    constructor(storeName: String?, userName: String?) {
        this.storeName = storeName
        this.userName = userName
    }

}