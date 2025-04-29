package com.example.echarging

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class StationViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _slots = MutableStateFlow<List<Slot>>(emptyList())
    val slots: StateFlow<List<Slot>> = _slots

    suspend fun loadSlots(stationId: String) {
        try {
            val documents = db.collection("Slots")
                .whereEqualTo("station_id", stationId)
                .get()
                .await()

            val slotList = documents.mapNotNull { it.toObject(Slot::class.java) }
            _slots.value = slotList
        } catch (e: Exception) {
        }
    }
}
