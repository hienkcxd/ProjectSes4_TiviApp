package com.example.projectses4_apptivi.model

class DeviceModel {
    var id:String ?= null
    var deviceID:String ?= null
    var deviceName:String ?= null
    var username:String ?= null
    var storeName:String ?= null
    var groupName:String ?= null
    var area:String ?= null
    var fileStorage:String ?= null

    constructor(){}
    constructor(
        deviceID: String?,
        deviceName: String?,
        username: String?,
        storeName: String?,
        groupName: String?,
        area: String?,
        fileStorage: String?
    ) {
        this.deviceID = deviceID
        this.deviceName = deviceName
        this.username = username
        this.storeName = storeName
        this.groupName = groupName
        this.area = area
        this.fileStorage = fileStorage
    }


}