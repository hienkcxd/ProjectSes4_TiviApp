package com.example.projectses4_apptivi.model

class UserModel {
    var id:String ?= null
    var email:String ?= null
    var name:String ?= null
    var password:String ?= null
    var phone:String ?= null
    var userName:String ?= null

    constructor(){}
    constructor(
        email: String?,
        name: String?,
        password: String?,
        phone: String?,
        userName: String?
    ) {
        this.email = email
        this.name = name
        this.password = password
        this.phone = phone
        this.userName = userName
    }
}