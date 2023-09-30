package com.example.globe_carry

//class Message {
//    var message: String? = null
//    var senderId:String? = null
//    var read: Boolean = false
//
//    constructor(){}
//
//    constructor(message: String?, senderId: String?,isRead: Boolean){
//        this.message = message
//        this.senderId = senderId
//        this.read = isRead
//    }
//}
data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var read: Boolean = false
)
