package kz.sd.kutpecalendar.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kz.sd.kutpecalendar.models.ScheduleItem

abstract class FRDBWrapper<T> {
    private val db = FirebaseDatabase.getInstance()

    protected abstract fun getTableName(): String
    protected abstract fun getClassType(): Class<T>

    private val _getDataLiveData = MutableLiveData<T?>()
    val getDataLiveData: LiveData<T?> = _getDataLiveData

    private val _updateLiveData = MutableLiveData<T?>()
    val updateLiveData: LiveData<T?> = _updateLiveData

    init {
        db.getReference(getTableName()).addValueEventListener(updateListener())
    }

    private fun updateListener() = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            _updateLiveData.postValue(snapshot.getValue(getClassType()))
        }

        override fun onCancelled(error: DatabaseError) {
            error.let {
                Log.e("FRDBWrapper", it.message)
            }
        }
    }

    fun saveData(value: T, successSave: ((Boolean) -> Unit)? = null) {
        db.getReference(getTableName()).setValue(value) { error, _ ->
            successSave?.invoke(error == null)
            error?.let {
                Log.e("FRDBWrapper", it.message)
            }
        }
    }

    fun changeAmountOfMoney(value: Double, savingId: String) {
        db.getReference(getTableName()).child("savings").child(savingId).child("amountOfMoney")
            .setValue(value)
    }
    fun changeNotes(value: String, savingId: String) {
        db.getReference(getTableName()).child("savings").child(savingId).child("note")
            .setValue(value)
    }
    fun changeTotalBalance(value:Double){
        db.getReference(getTableName()).child("totalBalance").setValue(value)
    }

    fun saveName(value: String) {
        db.getReference(getTableName()).child("name").setValue(value)
    }
    fun saveProfilePic(value: String) {
        db.getReference(getTableName()).child("pictureUrl").setValue(value)
    }


    fun saveItemToSchedule(value: ScheduleItem) {
        val Id = db.getReference(getTableName()).push().key
        if (Id != null) {
            db.getReference(getTableName()).child("schedule").child(Id).setValue(value)
        }
    }


    fun getData() {
        db.getReference(getTableName()).get().addOnSuccessListener {
            _getDataLiveData.postValue(it.getValue(getClassType()))
        }
    }

    fun removeData() {
        db.getReference(getTableName()).removeValue()
    }
}