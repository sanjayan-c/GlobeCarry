package com.example.globe_carry

import java.math.BigDecimal
import java.security.Timestamp
import java.util.Date

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
data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var read: Boolean = false
)
data class HomeItems(
    var no: String? = null,
    var city: String? = null,
    var country: String? = null,
    var type: String? = null,
    var weight: String? = null,
    var dimensions: String? = null,
    var charge: BigDecimal? = null,
    var expectedDate: String? = null,
    var time: String? = null,
    var date: String? = null,
    var urgent: Boolean? = null,
    )

data class Verification(
    var no: String? = null,
    var Fname:String?=null,
    var Lname:String?=null,
    var flightdate:String?=null,
    var orgin: String? = null,
    var city: String? = null,
    var country: String? = null,
    var requestedDate: String? = null,
    var time: String?? = null,
    var requestid: String? = null,
    var urgent: Boolean? = null,
    var travellerId:String?=null
)

data class MyDeliveries(
    var no: String? = null,
    var city: String? = null,
    var country: String? = null,
    var type: String? = null,
    var weight: String? = null,
    var dimensions: String? = null,
    var charge: BigDecimal? = null,
    var expectedDate: String? = null,
    var time: String? = null,
    var date: String? = null,
    var urgent: Boolean? = null,
)