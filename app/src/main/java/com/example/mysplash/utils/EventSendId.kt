package com.example.mysplash.utils

class EventSendId {
    data class OnSendIdToDetails(val id:String, val profileUrl:String,val pageFlag:String)
    data class OnSendIdToOneCollection(val id:String, val title:String,val backFlag:String,val enterFlag:String)
    data class OnSendSearchIdToDetails(val id:String, val profileUrl:String,val backFlag:String,val enterFlag:String)
    data class OnSendLangToStartInMAin(val id:String )
    data class OnSendViewPagerPosition(val position:Int )
}