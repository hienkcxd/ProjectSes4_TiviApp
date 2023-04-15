package com.example.projectses4_apptivi.model

class DeviceModel {
    var id:String ?= null
    var area:String ?= null
    var deviceId:String ?= null
    var deviceName:String ?= null
    var fileName:String ?= null
    var groupName:String ?= null
    var storename:String ?= null
    var userName:String ?= null

    constructor(){}
    constructor(
        area: String?,
        deviceId: String?,
        deviceName: String?,
        fileName: String?,
        groupName: String?,
        storename: String?,
        userName: String?
    ) {
        this.area = area
        this.deviceId = deviceId
        this.deviceName = deviceName
        this.fileName = fileName
        this.groupName = groupName
        this.storename = storename
        this.userName = userName
    }

}