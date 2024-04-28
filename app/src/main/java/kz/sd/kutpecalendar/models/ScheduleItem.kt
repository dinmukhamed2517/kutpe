package kz.sd.kutpecalendar.models

import java.util.Date

data class ScheduleItem(
    val id:Int? = null,
    val time: String? = null,
    val title: String? = null,
    val category:Category? = null,
    val date:Long? = null,
)


enum class Category{
    CAREER, PG, RELATIONSHIP, SPIRITUAL, FINANCE, FAMILY, FUN, HEALTH
}