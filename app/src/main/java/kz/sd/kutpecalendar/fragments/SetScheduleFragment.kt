package kz.sd.kutpecalendar.fragments

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sd.kutpecalendar.R
import kz.sd.kutpecalendar.adapters.CalendarAdapter
import kz.sd.kutpecalendar.base.BaseFragment
import kz.sd.kutpecalendar.databinding.FragmentSetScheduleBinding
import kz.sd.kutpecalendar.firebase.UserDao
import kz.sd.kutpecalendar.models.CalendarDateModel
import kz.sd.kutpecalendar.models.Category
import kz.sd.kutpecalendar.models.ScheduleItem
import kz.sd.kutpecalendar.utils.HorizontalItemDecoration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class SetScheduleFragment:BaseFragment<FragmentSetScheduleBinding>(FragmentSetScheduleBinding::inflate) {

    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override var showBottomNavigation: Boolean = false
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private var selectedCategory: Category? = null
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private val cnt = 0
    private lateinit var selectedTimeFrom:String
    private lateinit var selectedTimeTo:String



    override fun onBindView() {
        super.onBindView()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        setUpAdapter()
        setUpCalendar()
        setupCategorySelection()

        binding.from.setOnClickListener {
            showTimePickerDialog(requireContext()){hourOfDay, minute ->
                selectedTimeFrom = String.format("%02d:%02d", hourOfDay, minute)
                binding.from.text = selectedTimeFrom
            }
        }
        binding.to.setOnClickListener {
            showTimePickerDialog(requireContext()){hourOfDay, minute ->
                selectedTimeTo = String.format("%02d:%02d", hourOfDay, minute)
                binding.to.text = selectedTimeTo
            }
        }

        binding.saveBtn.setOnClickListener {
            if(selectedTimeFrom != null && selectedTimeTo != null && adapter.getSelectedDate()!= null){
                selectedCategory?.let { it1 ->
                    ScheduleItem(
                        cnt,
                        "$selectedTimeFrom - $selectedTimeTo",
                        binding.etNotes.text.toString(),
                        it1,
                        adapter.getSelectedDate()!!.time,
                        )
                }?.let { it2 ->
                    userDao.saveItemToSchedule(
                        it2
                    )
                }
                showCustomDialog("Success", "To do added to the schedule")
            }
            else{
                showCustomCancelDialog("Error",  "Please make sure that everything is selected correctly")
            }
        }
    }

    private fun setupCategorySelection() {
        val categoryCardViews = mapOf(
            binding.career to Category.CAREER,
            binding.pg to Category.PG,
            binding.relationship to Category.RELATIONSHIP,
            binding.spiritual to Category.SPIRITUAL,
            binding.finance to Category.FINANCE,
            binding.family to Category.FAMILY,
            binding.funn to Category.FUN,
            binding.health to Category.HEALTH
        )

        categoryCardViews.forEach { (cardView, category) ->
            cardView.setOnClickListener {
                selectedCategory = category
                highlightSelectedCategory(categoryCardViews.keys, cardView)
            }
        }
    }

    private fun highlightSelectedCategory(allCards: Set<CardView>, selectedCard: CardView) {
        val alphaFactor = 0.8f
        allCards.forEach { card ->
            val cardColor = (card.cardBackgroundColor?.defaultColor ?: R.color.white)
            card.setCardBackgroundColor(Color.argb((255 * alphaFactor).toInt(), Color.red(cardColor), Color.green(cardColor), Color.blue(cardColor)))
        }
        val selectedCardColor = (selectedCard.cardBackgroundColor?.defaultColor ?: R.color.white)
        selectedCard.setCardBackgroundColor(Color.argb(255, Color.red(selectedCardColor), Color.green(selectedCardColor), Color.blue(selectedCardColor)))
    }



    private fun setUpAdapter() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
        binding.recyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapter.setData(calendarList2)
        }
        binding.recyclerView.adapter = adapter
    }



    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
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

    fun showTimePickerDialog(context: Context, onTimeSet: (hourOfDay: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHourOfDay, selectedMinute ->
                onTimeSet(selectedHourOfDay, selectedMinute)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }


}