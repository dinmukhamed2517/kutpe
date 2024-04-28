package kz.sd.kutpecalendar.fragments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sd.kutpecalendar.R
import kz.sd.kutpecalendar.adapters.CalendarAdapter
import kz.sd.kutpecalendar.adapters.ScheduleAdapter
import kz.sd.kutpecalendar.base.BaseFragment
import kz.sd.kutpecalendar.databinding.ActivityMainBinding
import kz.sd.kutpecalendar.databinding.FragmentHomeBinding
import kz.sd.kutpecalendar.firebase.UserDao
import kz.sd.kutpecalendar.models.CalendarDateModel
import kz.sd.kutpecalendar.models.ScheduleItem
import kz.sd.kutpecalendar.utils.HorizontalItemDecoration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment:BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    var items: MutableList<ScheduleItem> = mutableListOf()



    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()

    private var selectedDate:Date? = null
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()



    override fun onBindView() {
        userDao.getData()
        super.onBindView()

        setUpAdapter()
        setUpCalendar()
        setUpClickListener()

        val scheduleAdapter = ScheduleAdapter()
        binding.scheduleRecycler.adapter = scheduleAdapter
        binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.addSpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_setScheduleFragment)
        }

        userDao.getDataLiveData.observe(this) { userData ->
            userData?.schedule?.values?.let { itemsList ->
                items.clear()

                val selectedDate = adapter.getSelectedDate()?.let { normalizeDate(it) }

                items.addAll(itemsList.filter { item ->
                    item.date?.let { normalizeDate(Date(it)) } == selectedDate
                })
                scheduleAdapter.submitList(items.toList()) // Ensure the list is updated
            }
        }
    }

    private fun normalizeDate(date: Date): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }



    private fun setUpClickListener() {
        binding.ivCalendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
        }
        binding.ivCalendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar()
        }
    }


    private fun setUpAdapter() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
        binding.recyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
                if (index == position){
                    selectedDate = calendarModel.data
                }
            }
            adapter.setData(calendarList2)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        binding.tvDateMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setData(calendarList)
    }
}