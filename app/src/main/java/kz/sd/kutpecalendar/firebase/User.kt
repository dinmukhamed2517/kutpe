package kz.sd.kutpecalendar.firebase

import kz.sd.kutpecalendar.models.ScheduleItem

data class User(
    var name:String? = null,
    var lastname:String?= null,
    var age:Int? = null,
    var phoneNumber:Long? =null,
    var pictureUrl: String? = null,
    var schedule:Map<String, ScheduleItem> = emptyMap(),
)