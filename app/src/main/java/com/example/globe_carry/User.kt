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
    var read: Boolean = false,
    var timeStamp: String = ""
)
data class HomeItems(
    var id: String? = null,
    var urgent: Boolean? = null,
    var image: String? = null,
    var category: String? = null,
    var content: String? = null,
    var value: Float? = null,
    var weight: String? = null,
    var dlvryAddress: String? = null,
    var city: String? = null,
    var country: String? = null,
    var recipient: String? = null,
    var rcptContactNo: String? = null,
    var dlvryDate: String? = null,
    var instructions: String? = null,
    var ttlCharge: Float? = null,
    var dimension: String? = null,
    var createdDate: String? = null,
    var createdBy: String? = null

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

//data class travellerItems(
//    var travellerId: String? = null,
//    var pasportImg: String? = null,
//    var travellerImg: String? = null,
//    var ticketImg: String? = null,
//    var passportNum: String? = null,
//    var flightDate: String? = null,
//    var destCountry: String? = null,
//    var destCity: String? = null,
//    var origin: String? = null,
//    var reqDate: String? = null,
//    var requId: String? = null
//
//    )

//object HomeItemsData {
//    var id: String? = null
//    var urgent: Boolean? = null
//    var image: String? = null
//    var category: String? = null
//    var content: String? = null
//    var value: Float? = null
//    var weight: String? = null
//    var dlvryAddress: String? = null
//    var city: String? = null
//    var country: String? = null
//    var recipient: String? = null
//    var rcptContactNo: String? = null
//    var dlvryDate: String? = null
//    var instructions: String? = null
//    var ttlCharge: Float? = null
//    var dimension: String? = null
//    var createdDate: String? = null
//    var createdBy: String? = null
//}
object DetailDataSingleton {
    var postId: String? = null
    var urgent: Boolean? = null
    var image: String? = null
    var category: String? = null
    var content: String? = null
    var value: Float? = null
    var weight: String? = null
    var dlvryAddress: String? = null
    var city: String? = null
    var country: String? = null
    var recipient: String? = null
    var rcptContactNo: String? = null
    var dlvryDate: String? = null
    var instructions: String? = null
    var ttlCharge: Float? = null
    var dimension: String? = null
    var createdDate: String? = null
    var createdBy: String? = null
}
object PassportImgSingleton {
    var passportImageBase64: String? = null
    // Add other properties or methods as needed
}

object TicketImgSingleton {
    var ticketImageBase64: String? = null
    // Add other properties or methods as needed
}
object TravellerImgSingleton {
    var travellerImageBase64: String? = null
    // Add other properties or methods as needed
}
