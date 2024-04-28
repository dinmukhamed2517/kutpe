package kz.sd.kutpecalendar.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor():ViewModel(){
    lateinit var name: String
    lateinit var lastname: String
    var phoneNumber: Long? = null
}