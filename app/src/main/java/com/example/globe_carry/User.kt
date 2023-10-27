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


    var createdBy: String? = null,
    var createdUserName: String? = null,
    var createdUserContactNo: String? = null,
    var notificationCount: Int? = null,
    var status: String? = null,
    var travellerName: String?=null,
    var travellerNum: String?=null,

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


data class MyDeliveryRequests(
    var postId: Int,
    var urgency: Boolean? = null,
    var category: String? = null,
    var content: String? = null,
    var value: BigDecimal? = BigDecimal.ZERO,
    var weight: BigDecimal? = BigDecimal.ZERO,
    var dlvryAddress: String? = null,
    var city: String? = null,
    var country: String? = null,
    var recipient: String? = null,
    var rcptContactNo: String? = null,
    var dlvryDate: String? = null,
    var instructions: String? = null,
    var ttlCharge: BigDecimal? = BigDecimal.ZERO,
    var dimension: String? = null,
    var createdDate: String? = null,
    var createdBy: String? = null,
    var imageBytes: String? = null,

    var orderstatus_id: Int,
    var received: Boolean? = null,
    var delivered: Boolean? = null,
    var paid: Boolean? = null,
    var departed: Boolean? = null,
    var reached: Boolean? = null,
    var orderStartedDate: String? = null,
    var orderStartedTime: String? = null,
    var orderCompletedDate: String? = null,
    var orderCompletedTime: String? = null,
    var orderReceivedDate: String? = null,
    var orderReceivedTime: String? = null,
    var orderDepartedDate: String? = null,
    var orderDepartedTime: String? = null,
    var orderReachedDate: String? = null,
    var orderReachedTime: String? = null,

    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNo: String? = null,
    var cityOrgin: String? = null,
    var countryOrgin: String? = null,

    var flightDate: String? = null,
    var passport: String? = null,
    var orgin: String? = null,
    var passportImage: String? = null,
    var ticketImage: String? = null,
    var travellerImage: String? = null,

    var myFirstName: String? = null,
    var myLastName: String? = null
)


data class CommentData(
    var comment: String = "",
    var commentGmail: String = "",
    var commentId: String = ""
)


object SingleProfile{
    var profileImage : String? = ""
}

