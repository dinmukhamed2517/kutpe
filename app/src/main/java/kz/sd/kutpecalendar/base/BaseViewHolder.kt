package kz.sd.kutpecalendar.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.sd.kutpecalendar.models.ScheduleItem

abstract class BaseViewHolder<VB : ViewBinding, T>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bindView(item: T)
}

abstract class BaseScheduleViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, ScheduleItem>(binding)