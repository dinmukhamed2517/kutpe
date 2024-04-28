package kz.sd.kutpecalendar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.sd.kutpecalendar.R
import kz.sd.kutpecalendar.base.BaseScheduleViewHolder
import kz.sd.kutpecalendar.databinding.ItemScheduleBinding
import kz.sd.kutpecalendar.models.ScheduleItem

class ScheduleAdapter:ListAdapter<ScheduleItem, BaseScheduleViewHolder<*>>(ScheduleDiffUtils()){

    class ScheduleDiffUtils:DiffUtil.ItemCallback<ScheduleItem>(){
        override fun areItemsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseScheduleViewHolder<*> {
        return ScheduleViewHolder(
            ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseScheduleViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ScheduleViewHolder(binding:ItemScheduleBinding):BaseScheduleViewHolder<ItemScheduleBinding>(binding){
        override fun bindView(item: ScheduleItem) {
            with(binding){
                scheduleTitle.text = item.title
                scheduleTime.text = item.time
            }
        }

    }
}