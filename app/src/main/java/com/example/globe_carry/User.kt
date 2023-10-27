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
    var createdBy: String? = null,
    var createdUserName: String? = null,
    var createdUserContactNo: String? = null,
    var notificationCount: Int? = null,
    var status: String? = null,
    var travellerName: String?=null,
    var travellerNum: String?=null,
)

data class TravelerDetails(
    val name: String,
    val phoneNo: String,
    val postID: String, // Add postID field
    val TravellerID: String, // Add TravellerID field
    val status: Int, // Add the status property
    val passportNo: String, // Add the status property
    val flightDate: String, // Add the status property
    val DestCountry: String, // Add the status property
    val DestCity: String, // Add the status property
    val Origin: String // Add the status property

)

object DetailDataSingleton {
    var postId: String? = null
    var urgent: Boolean? = null
   // var image: String? = null
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
object HomeItemImageSingleton {
    var itemImageBase64: String? = null
}
object TravelerDetailsSingleton {
    var travelerName: String = ""
    var travelerPhoneNo: String = ""
    var travelerId: String = ""
}
