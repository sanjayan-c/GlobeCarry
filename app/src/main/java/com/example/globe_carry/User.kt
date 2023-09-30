package com.example.globe_carry

class User {
    var name:String? = null
    var email:String? = null
    var uid:String? = null
    var type:String? = null
    constructor(){}

    constructor(name:String?,email:String?,uid:String?,type:String?){
        this.name=name
        this.email=email
        this.uid=uid
        this.type=type
    }
}